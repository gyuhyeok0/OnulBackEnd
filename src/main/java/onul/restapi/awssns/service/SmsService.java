package onul.restapi.awssns.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import onul.restapi.awssns.entity.CodeEntity;
import onul.restapi.awssns.repository.CodeRepository;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate; // ✅ Redis 사용
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
@Transactional
public class SmsService {

    private final SnsClient snsClient;
    private final CodeRepository codeRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate; // ✅ Redis 주입

    private final int REQUEST_LIMIT = 4;
    private final int DAILY_REQUEST_LIMIT = 8;

    public SmsService(@Value("${aws.access-key}") String accessKey,
                      @Value("${aws.secret-key}") String secretKey,
                      @Value("${aws.region}") String region,
                      CodeRepository codeRepository,
                      MemberRepository membersRepository,
                      RedisTemplate<String, String> redisTemplate) { // ✅ Redis 주입
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.snsClient = SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        this.codeRepository = codeRepository;
        this.memberRepository = membersRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public SmsResponse sendSms(String phoneNumber, boolean skipPhoneNumberCheck) {
        String hashedPhoneNumber = hashPhoneNumber(phoneNumber); // SHA-256 해시 ✅ 유지
        long currentTime = System.currentTimeMillis();

        // 이미 등록된 번호인지 확인
        if (!skipPhoneNumberCheck && isPhoneNumberRegistered(hashedPhoneNumber)) {
            return new SmsResponse("alreadyPhoneNumber");
        }

        // ✅ Redis 기반 요청 제한 체크 시작
        String hourKey = "sms:" + hashedPhoneNumber + ":hour";
        String dayKey = "sms:" + hashedPhoneNumber + ":day";

        Long hourlyCount = redisTemplate.opsForValue().increment(hourKey);
        if (hourlyCount != null && hourlyCount == 1) {
            redisTemplate.expire(hourKey, 1, TimeUnit.HOURS);
        }
        if (hourlyCount != null && hourlyCount > REQUEST_LIMIT) {
            return new SmsResponse("LIMIT_EXCEEDED");
        }

        Long dailyCount = redisTemplate.opsForValue().increment(dayKey);
        if (dailyCount != null && dailyCount == 1) {
            redisTemplate.expire(dayKey, 1, TimeUnit.DAYS);
        }
        if (dailyCount != null && dailyCount > DAILY_REQUEST_LIMIT) {
            return new SmsResponse("DAILY_LIMIT_EXCEEDED");
        }
        // ✅ Redis 기반 요청 제한 체크 끝

        // 인증 코드 생성
        String verificationCode = generateVerificationCode();
        String message = String.format("Welcome to onul, your verification code is %s", verificationCode);

        PublishRequest request = PublishRequest.builder()
                .message(message)
                .phoneNumber(phoneNumber)
                .build();

        PublishResponse result = snsClient.publish(request);

        // ✅ DB에서 기존 엔티티를 가져오거나 새로 생성
        CodeEntity codeEntity = codeRepository.findByPhoneNumber(hashedPhoneNumber);
        if (codeEntity == null) {
            codeEntity = new CodeEntity(hashedPhoneNumber, verificationCode, currentTime + TimeUnit.MINUTES.toMillis(5), currentTime);
        } else {
            codeEntity.setCode(verificationCode);
            codeEntity.setExpiryTime(currentTime + TimeUnit.MINUTES.toMillis(5));
        }

        codeEntity.setRequestCount((int) (hourlyCount != null ? hourlyCount : 1));
        codeEntity.setDailyRequestCount((int) (dailyCount != null ? dailyCount : 1));
        codeEntity.setLastRequestTime(currentTime);
        codeEntity.setLastRequestDay(currentTime);
        codeRepository.save(codeEntity); // ✅ save 1회만

        return new SmsResponse("success");
    }

    private boolean isPhoneNumberRegistered(String hashedPhoneNumber) {
        return memberRepository.existsByMemberPhoneNumber(hashedPhoneNumber);
    }

    public String hashPhoneNumber(String phoneNumber) {
        String normalizedPhoneNumber = normalizePhoneNumber(phoneNumber);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // ✅ 유지
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

    public String normalizePhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("\\D", "");
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
