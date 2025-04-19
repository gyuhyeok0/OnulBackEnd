package onul.restapi.auth.apple.service;

import org.json.simple.JSONObject;

/**
 * Apple 로그인 로직을 정의하는 서비스 인터페이스
 * - identityToken 검증
 * - 회원 가입 또는 로그인 처리
 * - JWT 발급 후 응답
 */
public interface AppleAuthService {

    /**
     * Apple identityToken을 검증하고,
     * 필요한 경우 회원가입 후 JWT 토큰을 발급함
     *
     * @param identityToken 클라이언트에서 전달된 Apple JWT
     * @return JSONObject 형태의 로그인 응답 (기존 방식과 동일)
     * @throws Exception 토큰 검증 실패 등 에러 발생 시
     */
    JSONObject authenticateWithApple(String identityToken) throws Exception;
}
