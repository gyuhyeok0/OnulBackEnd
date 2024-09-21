package onul.restapi.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("statusMessage")
    private String statusMessage;

    public TokenResponse() {}

    public TokenResponse(String accessToken, String refreshToken, String statusMessage) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.statusMessage = statusMessage;
    }

    // Getter 및 Setter
    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
