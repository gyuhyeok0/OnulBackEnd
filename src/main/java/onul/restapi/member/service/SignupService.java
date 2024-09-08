package onul.restapi.member.service;

import jakarta.transaction.Transactional;
import onul.restapi.member.entity.MemberStatus;
import onul.restapi.member.entity.Members;
import onul.restapi.member.entity.UserConsentState;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class SignupService {

    private final MemberRepository memberRepository;

    public SignupService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원가입 메서드 (핸드폰 번호 추가)
    @Transactional
    public boolean signup(String memberId, String memberPassword, String memberCountryCode, String memberPhoneNumber) {

        try {
            // 비밀번호 해시화
            String hashedPassword = hashPassword(memberPassword);

            // 핸드폰 번호 해시화
            String hashedPhoneNumber = hashPhoneNumber(memberPhoneNumber);
            System.out.println(hashedPhoneNumber);

            // 사용자 정보 저장 (핸드폰 번호 추가)
            saveUser(memberId, hashedPassword, memberCountryCode, hashedPhoneNumber);

            return true;
        } catch (Exception e) {
            // 에러가 발생한 경우 false 반환
            e.printStackTrace();
            return false;
        }
    }

    // 비밀번호 해시화 메서드
    private String hashPassword(String password) {
        // BCrypt 해시화 사용
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public String normalizePhoneNumber(String phoneNumber) {
        // 국가 코드 제거 및 하이픈, 공백 제거 예시
        return phoneNumber.replaceAll("\\D", ""); // 숫자만 남기기
    }

    // 해시 함수 수정
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

    // 사용자 정보 저장 (회원가입 일시와 enum 처리 포함)
    private void saveUser(String memberId, String hashedPassword, String memberCountryCode, String hashedPhoneNumber) {
        // 회원 가입 일시
        Date signupDate = new Date();

        // 기본 약관 동의 상태와 회원 상태 설정
        UserConsentState consentState = UserConsentState.AGREED; // 동의로 저장
        MemberStatus memberStatus = MemberStatus.ACTIVE; // 회원상태 Y 로 저장

        // Members 엔티티 빌드
        Members newMember = new Members(
                memberId,
                hashedPassword,
                hashedPhoneNumber,
                memberCountryCode,
                consentState,
                signupDate,
                memberStatus
        );

        System.out.println("너도 실행 됨? " + newMember);

        // 데이터베이스에 저장
        memberRepository.save(newMember);
    }
}
