package onul.restapi.awssns.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import jakarta.transaction.Transactional;
import onul.restapi.awssns.entity.CodeEntity;
import onul.restapi.awssns.repository.CodeRepository;
import onul.restapi.member.repository.MemberRepository;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static javax.management.timer.Timer.ONE_DAY;

@Service
@Transactional
public class SmsService {

    private final SnsClient snsClient;
    private final CodeRepository codeRepository;
    private final MemberRepository memberRepository;
    private final int REQUEST_LIMIT = 4; // 최대 요청 횟수
    private final long TIME_LIMIT = TimeUnit.HOURS.toMillis(1); // 제한 시간 1시간
    private final int DAILY_REQUEST_LIMIT = 8; // 하루 최대 요청 횟수

    // aws sms 셋팅
    public SmsService(@Value("${aws.access-key}") String accessKey,
                      @Value("${aws.secret-key}") String secretKey,
                      @Value("${aws.region}") String region,
                      CodeRepository codeRepository,
                      MemberRepository membersRepository) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.snsClient = SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        this.codeRepository = codeRepository;
        this.memberRepository = membersRepository;
    }

    // 인증코드 전송
    public SmsResponse sendSms(String phoneNumber, boolean skipPhoneNumberCheck) {

        String hashedPhoneNumber = hashPhoneNumber(phoneNumber); // 전화번호 해시화
        long currentTime = System.currentTimeMillis();

        // 전화번호 등록 여부 확인을 생략할지 여부를 확인
        if (!skipPhoneNumberCheck && isPhoneNumberRegistered(hashedPhoneNumber)) {
            return new SmsResponse("alreadyPhoneNumber");  // 상태만 반환
        }

        // 기존 인증 코드 확인
        Optional<CodeEntity> existingCode = Optional.ofNullable(codeRepository.findByPhoneNumber(hashedPhoneNumber));

        if (existingCode.isPresent()) {
            CodeEntity codeEntity = existingCode.get();
            long lastRequestTime = codeEntity.getLastRequestTime();
            long timeSinceLastRequest = currentTime - lastRequestTime;
            long lastRequestDay = codeEntity.getLastRequestDay();
            long timeSinceLastDay = currentTime - lastRequestDay;

            // 하루 제한 체크
            if (timeSinceLastDay < ONE_DAY) {
                if (codeEntity.getDailyRequestCount() >= DAILY_REQUEST_LIMIT) {
                    return new SmsResponse("DAILY_LIMIT_EXCEEDED"); // 일일 요청 제한 초과
                } else {
                    // 요청 횟수 증가
                    codeEntity.setDailyRequestCount(codeEntity.getDailyRequestCount() + 1);
                }
            } else {
                // 하루가 지났다면 일일 요청 횟수 초기화
                codeEntity.setDailyRequestCount(1);
                codeEntity.setLastRequestDay(currentTime); // 일일 제한 갱신
            }

            // 시간 제한 안에 요청한 경우
            if (timeSinceLastRequest < TIME_LIMIT) {
                // 요청 횟수 초과한 경우
                if (codeEntity.getRequestCount() >= REQUEST_LIMIT) {
                    return new SmsResponse("LIMIT_EXCEEDED"); // 요청 제한 초과
                } else {
                    System.out.println("1");

                    // 요청 횟수 증가 및 시간 갱신
                    codeEntity.setRequestCount(codeEntity.getRequestCount() + 1);
                    codeEntity.setLastRequestTime(currentTime); // 마지막 요청 시간 갱신
                    codeRepository.save(codeEntity);
                }
            } else {
                // 제한 시간을 초과하면 요청 횟수 초기화
                codeEntity.setRequestCount(1); // 요청 횟수 초기화
                codeEntity.setLastRequestTime(currentTime); // 마지막 요청 시간 갱신
                codeRepository.save(codeEntity);
            }

            System.out.println("5");
            // 새로운 인증 코드를 생성하여 업데이트
            String newVerificationCode = generateVerificationCode();
            codeEntity.setCode(newVerificationCode); // 새로운 인증 코드 설정
            codeEntity.setExpiryTime(currentTime + TimeUnit.MINUTES.toMillis(5)); // 인증 코드 유효 시간 갱신
            codeRepository.save(codeEntity); // 저장

        } else {
            // 처음 요청일 경우, 새로운 인증 코드 생성 및 저장
            String verificationCode = generateVerificationCode();
            String message = String.format("Welcome to onul, your verification code is %s", verificationCode);

            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .phoneNumber(phoneNumber)
                    .build();

            PublishResponse result = snsClient.publish(request);

            // 새로운 인증 코드 데이터베이스 저장 (12시간 유효)
            CodeEntity newCodeEntity = new CodeEntity(hashedPhoneNumber, verificationCode, currentTime + TimeUnit.MINUTES.toMillis(5), currentTime);
            newCodeEntity.setRequestCount(1); // 첫 요청
            newCodeEntity.setLastRequestTime(currentTime); // 첫 요청 시간 저장
            newCodeEntity.setDailyRequestCount(1); // 첫 일일 요청으로 설정
            newCodeEntity.setLastRequestDay(currentTime); // 일일 제한 시간 저장
            codeRepository.save(newCodeEntity);
        }

        return new SmsResponse("success");  // 상태만 반환
    }




    // 전화번호가 이미 등록된 회원인지 확인
    private boolean isPhoneNumberRegistered(String hashedPhoneNumber) {
        return memberRepository.existsByMemberPhoneNumber(hashedPhoneNumber);
    }



    public String hashPhoneNumber(String phoneNumber) {

        String normalizedPhoneNumber = normalizePhoneNumber(phoneNumber); // 전화번호 정규화
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(normalizedPhoneNumber.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 알고리즘이 존재하지 않습니다.");
        }
    }

    // 전화번호 특수기호 빼기
    public String normalizePhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("\\D", ""); // 숫자만 남기기
    }

    // 인증 코드 생성
    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
