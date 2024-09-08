package onul.restapi.member.entity;

public enum UserConsentState {
    AGREED("Y"),  // 동의
    NOT_AGREED("N"); // 비동의

    private final String code;

    UserConsentState(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
