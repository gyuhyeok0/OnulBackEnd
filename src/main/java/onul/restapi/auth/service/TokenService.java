package onul.restapi.auth.service;

import onul.restapi.auth.dto.TokenResponse;
import onul.restapi.member.dto.MemberDTO;
import onul.restapi.member.service.RedisService;
import onul.restapi.util.TokenUtils;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenUtils tokenUtils;
    private final RedisService redisService;

    public TokenService(TokenUtils tokenUtils, RedisService redisService) {
        this.tokenUtils = tokenUtils;
        this.redisService = redisService;
    }

    public TokenResponse refreshAccessToken(String refreshToken, String memberId) {
        // 1. MemberDTO 객체 생성
        MemberDTO member = new MemberDTO();
        member.setMemberId(memberId); // 멤버 ID 설정

        // 2. 리프레시 토큰이 존재하는지 확인
        if (redisService.getRefreshToken(memberId) == null) {

            return null; // null 반환
        }

        // 3. 새로운 액세스 토큰 생성
        String newAccessToken = tokenUtils.generateJwtToken(member); // 액세스 토큰 생성



        // 4. 리프레시 토큰의 만료 시간 확인
        if (tokenUtils.isRefreshTokenExpired(refreshToken)) {
            // 리프레시 토큰이 만료된 경우, 새로운 리프레시 토큰 생성
            String newRefreshToken = tokenUtils.generateRefreshToken(member); // 새로운 리프레시 토큰 생성
            // 클라이언트에게 새로운 리프레시 토큰 전달
            return new TokenResponse(newAccessToken, newRefreshToken, null); // 새로운 액세스 토큰과 리프레시 토큰 반환
        }

        return new TokenResponse(newAccessToken, null,null); // 새로운 액세스 토큰 반환
    }





}
