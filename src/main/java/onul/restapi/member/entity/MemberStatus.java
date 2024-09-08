package onul.restapi.member.entity;

public enum MemberStatus {
    ACTIVE("Y"),  // 활성
    INACTIVE("N"); // 비활성 (탈퇴)

    private final String code;

    MemberStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
