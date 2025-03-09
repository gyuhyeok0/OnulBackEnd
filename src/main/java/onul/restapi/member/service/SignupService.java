package onul.restapi.member.service;

import onul.restapi.member.entity.MemberStatus;
import onul.restapi.member.entity.Members;
import onul.restapi.member.entity.UserConsentState;
import onul.restapi.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

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

        Date signupDate = new Date(System.currentTimeMillis());

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

        // 데이터베이스에 저장
        memberRepository.save(newMember);
    }


    // 아이디 중복 확인
    public boolean isMemberIdDuplicate(String memberId) {
        return memberRepository.existsByMemberId(memberId);  // DB에 해당 memberId가 있는지 확인
    }


    // 비밀번호 재설정
    @Transactional
    public boolean updatePassword(String memberId, String memberPassword) {
        // 사용자 ID로 해당 사용자를 찾습니다.
        Optional<Members> memberOpt = Optional.ofNullable(memberRepository.findByMemberId(memberId));

        // 사용자가 존재하면
        if (memberOpt.isPresent()) {
            Members member = memberOpt.get();

            // 비밀번호 암호화 (PasswordEncoder를 사용하여 비밀번호를 암호화합니다.)
            String encodedPassword = hashPassword(memberPassword);

            // 사용자 객체의 비밀번호를 업데이트합니다.
            member.setMemberPassword(encodedPassword);

            // 변경된 사용자 정보를 저장합니다.
            memberRepository.save(member);

            // 비밀번호가 성공적으로 업데이트되었으므로 true 반환
            return true;
        }

        // 사용자가 존재하지 않으면 false 반환
        return false;
    }




}
