package onul.restapi.auth.controller;

import onul.restapi.auth.dto.TokenRequest;
import onul.restapi.auth.dto.TokenResponse;
import onul.restapi.auth.service.TokenService;
import onul.restapi.member.controller.StateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class TokenController {

    @Autowired
    private TokenService tokenService; // 토큰 서비스를 주입받습니다.

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(@RequestBody TokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken(); // 요청에서 리프레시 토큰 가져오기
        String memberId = tokenRequest.getMemberId(); // 요청에서 멤버 ID 가져오기

        // 새로운 액세스 토큰 및 리프레시 토큰 생성
        TokenResponse tokenResponse = tokenService.refreshAccessToken(refreshToken, memberId);

        if (tokenResponse == null) {
            String errorMessage = "Refresh token not found";

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new TokenResponse(null, null, errorMessage)); // 상태 메시지와 함께 반환
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON) // JSON 타입 명시
                .body(tokenResponse); // 새로운 액세스 토큰 반환
    }



}
