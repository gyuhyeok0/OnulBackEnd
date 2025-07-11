package onul.restapi.auth.handler;

import onul.restapi.common.AuthConstants;
import onul.restapi.member.dto.MemberDTO;
import onul.restapi.member.dto.TokenDTO;
import onul.restapi.util.ConvertUtil;
import onul.restapi.util.TokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final TokenUtils tokenUtils;  // TokenUtils 인스턴스를 주입받기 위한 필드

    // TokenUtils 주입
    @Autowired
    public CustomAuthSuccessHandler(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        MemberDTO member = ((MemberDTO) authentication.getPrincipal());

        HashMap<String,Object> responseMap = new HashMap<>();
        JSONObject jsonValue = null;
        JSONObject jsonObject;

        // 회원 탈퇴 상태 확인
        if(member.getMemberStatus().equals("N")){
            responseMap.put("userInfo", jsonValue);
            responseMap.put("status", 500);
            responseMap.put("message", "회원 탈퇴 된 아이디 입니다.");
        } else {
            // Access Token 생성
            String accessToken = tokenUtils.generateJwtToken(member);

            // Refresh Token 생성
            String refreshToken = tokenUtils.generateRefreshToken(member);  // 인스턴스 메서드로 변경

            // TokenDTO에 Access Token과 Refresh Token을 포함
            TokenDTO tokenDTO = TokenDTO.builder()
                    .memberId(member.getMemberId())
                    .memberSignupDate(member.getMemberSignupDate())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)  // Refresh Token 추가
                    .grantType(AuthConstants.TOKEN_TYPE)
                    .build();

            jsonValue = (JSONObject) ConvertUtil.convertObjectToJsonObject(tokenDTO);

            responseMap.put("userInfo", jsonValue);
            responseMap.put("status", 200);
            responseMap.put("message", "로그인 성공");
        }

        jsonObject = new JSONObject(responseMap);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}
