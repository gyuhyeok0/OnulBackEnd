package onul.restapi.auth.dto;

public class TokenRequest {
    private String refreshToken;
    private String memberId;

    public TokenRequest() {
    }

    public TokenRequest(String refreshToken, String memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
