package onul.restapi.auth.filter;

import onul.restapi.common.AuthConstants;
import onul.restapi.member.dto.MemberDTO;
import onul.restapi.util.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final TokenUtils tokenUtils;

    // 초당 최대 요청 수
    private static final int MAX_REQUESTS_PER_SECOND = 20;
    private static final long TIME_WINDOW = TimeUnit.SECONDS.toMillis(1);  // 1초 윈도우

    private static Map<String, Long> requestCount = new HashMap<>();
    private static Map<String, Long> lastRequestTime = new HashMap<>();

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        super(authenticationManager);
        this.tokenUtils = tokenUtils; // TokenUtils 주입
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {



        // API 호출을 제외할 URL 패턴 설정
        List<String> roleLeessList = Arrays.asList(

                "/internal/(.*)",
                "/actuator/(.*)",
                "/webinquiry",
                "/auth/apple",

                "/",
                "/ads.txt",
                "/app-ads.txt",
                "/images/(.*)",

                "/auth/autoAdapt",
                "/analysis/update",
                "/appVersion/(.*)",
                "/auth/refresh",
                "/inspection/(.*)",
                "/privacy-policy.html",
                "/terms-of-service.html",

                "/signup/(.*)",
                "/privacy-policy/(.*)",
                "/sms/(.*)",
                "/inquiry/(.*)",
                "/members/(.*)",
                "/members/employee/soft-delete",
                "/api/v1/products/\\d+",
                "/api/v1/products/\\w+",
                "/api/v1/products",
                "/api/v1/reviews/product/\\d+",
                "/api/v1/products/search?s=\\w+",
                "/auth/signup",
                "/auth/login",
                "/auth/inquiry",
                "/api/v1/reviews",
                "/api/v1/reviews/\\d++",
                "/api/v1/reviews/(\\d+)?offset=\\d+"
        );

        // 인증되지 않은 URL은 그대로 통과
        if (roleLeessList.stream().anyMatch(pattern -> Pattern.matches(pattern, request.getRequestURI()))) {
            chain.doFilter(request, response);
            return;
        }

        String clientIp = request.getRemoteAddr(); // 클라이언트 IP 주소
        long currentTime = System.currentTimeMillis(); // 현재 시간

        // 초당 요청 수 제한 체크
        if (lastRequestTime.containsKey(clientIp) && currentTime - lastRequestTime.get(clientIp) <= TIME_WINDOW) {
            long requestCountForIp = requestCount.getOrDefault(clientIp, 0L);
            if (requestCountForIp >= MAX_REQUESTS_PER_SECOND) {
                // 초과 요청에 대해서 429 상태 코드 반환
                response.setStatus(429); // HTTP 429 상태 코드 (Too Many Requests)
                response.getWriter().write("{\"status\": 429, \"message\": \"Too Many Requests\"}");
                return; // 더 이상 처리하지 않음
            }
            requestCount.put(clientIp, requestCountForIp + 1);
        } else {
            // 새로운 시간 윈도우 시작
            requestCount.put(clientIp, 1L);
        }

        lastRequestTime.put(clientIp, currentTime); // 마지막 요청 시간 기록

        String header = request.getHeader(AuthConstants.AUTH_HEADER);

        try {
            if (header != null && !header.equalsIgnoreCase("")) {
                // TokenUtils를 인스턴스 메서드로 호출
                String token = tokenUtils.splitHeader(header);

                if (tokenUtils.isValidToken(token)) {

                    Claims claims = tokenUtils.getClaimsFromToken(token);

                    MemberDTO authentication = new MemberDTO();
                    authentication.setMemberId(claims.get("memberId").toString());

                    AbstractAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(authentication, token, authentication.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    chain.doFilter(request, response);
                } else {
                    throw new RuntimeException("토큰이 유효하지 않습니다");
                }
            } else {
                throw new RuntimeException("토큰이 존재하지 않습니다");
            }
        } catch (Exception e) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            // 상태 코드 설정 (예: 401 Unauthorized)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", HttpServletResponse.SC_UNAUTHORIZED); // 상태 코드만 포함

            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }
    }

    private JSONObject jsonresponseWrapper(Exception e) {
        String resultMsg = "";
        if (e instanceof ExpiredJwtException) {
            resultMsg = "Token Expired";
        } else if (e instanceof JwtException) {
            resultMsg = "Token parsing JwtException";
        } else {
            resultMsg = "Other token error";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("message", resultMsg);
        jsonMap.put("reason", e.getMessage());
        return new JSONObject(jsonMap);
    }
}
