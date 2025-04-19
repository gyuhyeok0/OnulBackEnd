package onul.restapi.auth.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Apple 로그인 성공 시 반환되는 응답 DTO
 * accessToken, refreshToken, memberId 정보를 포함함
 */
@Data
@AllArgsConstructor
public class AppleLoginResponse {
    private String accessToken;
    private String refreshToken;
    private String memberId;
}
