package onul.restapi.auth.apple.controller;

import lombok.RequiredArgsConstructor;
import onul.restapi.auth.apple.service.AppleAuthService;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Apple 로그인 요청을 처리하는 컨트롤러
 * 프론트엔드에서 전달된 identityToken을 받아 Apple 서버에서 검증하고,
 * 유효한 경우 회원 가입 또는 로그인 처리 후 JWT를 발급해 응답함
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AppleLoginController {

    private final AppleAuthService appleAuthService;

    /**
     * Apple 로그인 엔드포인트
     * @param body JSON 형식으로 전달된 identityToken
     * @return 기존 로그인과 동일한 JSON 응답 (accessToken, refreshToken, memberId 등 포함)
     */
    @PostMapping(value = "/apple", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginWithApple(@RequestBody Map<String, String> body) {
        String identityToken = body.get("identityToken");

        if (identityToken == null || identityToken.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("status", 400, "message", "identityToken is missing"));
        }

        try {
            Map<String, Object> result = appleAuthService.authenticateWithApple(identityToken);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Apple login failed: " + e.getMessage()));
        }
    }
}
