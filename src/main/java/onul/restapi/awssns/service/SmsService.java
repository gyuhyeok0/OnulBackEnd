package onul.restapi.awssns.service;

import jakarta.transaction.Transactional;
import onul.restapi.awssns.entity.CodeEntity;
import onul.restapi.awssns.repository.CodeRepository;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class SmsService {

    private final SnsClient snsClient;
    private final CodeRepository codeRepository;

    public SmsService(@Value("${aws.access-key}") String accessKey,
                      @Value("${aws.secret-key}") String secretKey,
                      @Value("${aws.region}") String region,
                      CodeRepository codeRepository) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.snsClient = SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        this.codeRepository = codeRepository;
    }

    public String sendSms(String phoneNumber) {
        // 기존 인증 코드가 있는지 확인
        Optional<CodeEntity> existingCode = Optional.ofNullable(codeRepository.findByPhoneNumber(phoneNumber));

        // 기존 인증 코드가 있으면 삭제
        existingCode.ifPresent(codeEntity -> codeRepository.deleteByPhoneNumber(phoneNumber));

        // 새로운 인증 코드 생성 및 전송
        String verificationCode = generateVerificationCode();
        String message = String.format("Welcome to onul, your verification code is %s", verificationCode);

        PublishRequest request = PublishRequest.builder()
                .message(message)
                .phoneNumber(phoneNumber)
                .build();

        PublishResponse result = snsClient.publish(request);

        // 데이터베이스에 새로운 인증 코드 저장
        CodeEntity newCodeEntity = new CodeEntity(phoneNumber, verificationCode, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)); // 5분 유효
        codeRepository.save(newCodeEntity);

        return result.messageId();
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
