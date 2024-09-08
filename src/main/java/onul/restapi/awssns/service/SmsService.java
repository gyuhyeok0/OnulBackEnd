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

@Service
@Transactional
public class SmsService {

    private final SnsClient snsClient;
    private final CodeRepository codeRepository;
    private final MemberRepository memberRepository;

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
    public SmsResponse sendSms(String phoneNumber) {
        String hashedPhoneNumber = hashPhoneNumber(phoneNumber); // 전화번호 해시화

        // 전화번호 등록 여부 확인
        if (isPhoneNumberRegistered(hashedPhoneNumber)) {
            System.out.println("이미 등록된 전화번호입니다.");
            return new SmsResponse("alreadyPhoneNumber");  // 상태만 반환
        }

        // 기존 인증 코드가 있는지 확인 후 삭제
        Optional<CodeEntity> existingCode = Optional.ofNullable(codeRepository.findByPhoneNumber(hashedPhoneNumber));
        existingCode.ifPresent(codeEntity -> codeRepository.deleteByPhoneNumber(hashedPhoneNumber));

        // 인증 코드 생성 및 SNS 전송
        String verificationCode = generateVerificationCode();
        String message = String.format("Welcome to onul, your verification code is %s", verificationCode);

        PublishRequest request = PublishRequest.builder()
                .message(message)
                .phoneNumber(phoneNumber)
                .build();

        PublishResponse result = snsClient.publish(request);

        // 새로운 인증 코드 데이터베이스 저장 (12시간 유효)
        CodeEntity newCodeEntity = new CodeEntity(hashedPhoneNumber, verificationCode, System.currentTimeMillis() + TimeUnit.HOURS.toMillis(12));
        codeRepository.save(newCodeEntity);

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
