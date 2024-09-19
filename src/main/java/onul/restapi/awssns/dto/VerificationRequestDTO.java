package onul.restapi.awssns.dto;

public class VerificationRequestDTO {

    private String phoneNumber;
    private String code;
    private String memberId; // 추가된 필드

    public VerificationRequestDTO() {
    }

    public VerificationRequestDTO(String phoneNumber, String code, String memberId) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.memberId = memberId; // 추가된 부분
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemberId() {
        return memberId; // 추가된 부분
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId; // 추가된 부분
    }

    @Override
    public String toString() {
        return "VerificationRequestDTO{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", code='" + code + '\'' +
                ", memberId='" + memberId + '\'' + // 추가된 부분
                '}';
    }
}
