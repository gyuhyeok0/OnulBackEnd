package onul.restapi.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthFailUserHandler implements AuthenticationFailureHandler {

    private static final Map<Class<? extends AuthenticationException>, String> exceptionMessages = new HashMap<>();

    static {
        exceptionMessages.put(AuthenticationServiceException.class, "존재하지 않는 사용자입니다");
        exceptionMessages.put(BadCredentialsException.class, "아이디 또는 비밀번호가 일치하지 않습니다");
        exceptionMessages.put(LockedException.class, "잠긴 계정입니다");
        exceptionMessages.put(DisabledException.class, "비활성 계정입니다");
        exceptionMessages.put(AccountExpiredException.class, "만료된 계정입니다");
        exceptionMessages.put(CredentialsExpiredException.class, "자격증명이 만료되었습니다");
        exceptionMessages.put(AuthenticationCredentialsNotFoundException.class, "인증요청이 거부되었습니다");
        exceptionMessages.put(UsernameNotFoundException.class, "존재하지 않는 사용자입니다");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String failMsg = exceptionMessages.getOrDefault(exception.getClass(), "정의되어 있지 않는 케이스의 오류입니다");

        // 터미널에 로그 출력
        System.out.println("Authentication failed: " + failMsg);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("failType", failMsg);

        JSONObject jsonObject = new JSONObject(resultMap);

        printWriter.println(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}
