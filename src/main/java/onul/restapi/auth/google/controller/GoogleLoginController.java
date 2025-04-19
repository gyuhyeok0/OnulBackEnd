package onul.restapi.auth.google.controller;

import lombok.RequiredArgsConstructor;
import onul.restapi.auth.google.service.GoogleAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Google 로그인 요청을 처리하는 컨트롤러
 * 프론트엔드에서 전달된 idToken을 받아 Google 서버에서 검증하고,
 * 유효한 경우 회원 가입 또는 로그인 처리 후 JWT를 발급해 응답함
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class GoogleLoginController {

    private final GoogleAuthService googleAuthService;

    /**
     * Google 로그인 엔드포인트
     * @param body JSON 형식으로 전달된 idToken
     * @return accessToken, refreshToken, memberId 포함한 응답
     */
    @PostMapping(value = "/google", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");

        if (idToken == null || idToken.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("status", 400, "message", "idToken is missing"));
        }

        try {
            Map<String, Object> result = googleAuthService.authenticateWithGoogle(idToken);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Google login failed: " + e.getMessage()));
        }
    }
}
