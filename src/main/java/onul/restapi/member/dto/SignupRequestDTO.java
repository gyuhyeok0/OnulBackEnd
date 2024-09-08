package onul.restapi.member.dto;


// 회원가입 인증 관련
public class SignupRequestDTO {
    private String memberId;
    private String memberPassword;
    private String memberCountryCode;
    private boolean agreed;
    private String memberPhoneNumber;

    // 기본 생성자
    public SignupRequestDTO() {
    }

    public SignupRequestDTO(String memberId, String memberPassword, String memberCountryCode, boolean agreed, String memberPhoneNumber) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberCountryCode = memberCountryCode;
        this.agreed = agreed;
        this.memberPhoneNumber = memberPhoneNumber;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public String getMemberCountryCode() {
        return memberCountryCode;
    }

    public void setMemberCountryCode(String memberCountryCode) {
        this.memberCountryCode = memberCountryCode;
    }

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }

    public String getMemberPhoneNumber() {
        return memberPhoneNumber;
    }

    public void setMemberPhoneNumber(String memberPhoneNumber) {
        this.memberPhoneNumber = memberPhoneNumber;
    }

    @Override
    public String toString() {
        return "SignupRequestDTO{" +
                "memberId='" + memberId + '\'' +
                ", memberPassword='" + memberPassword + '\'' +
                ", memberCountryCode='" + memberCountryCode + '\'' +
                ", agreed=" + agreed +
                ", memberPhoneNumber='" + memberPhoneNumber + '\'' +
                '}';
    }
}
