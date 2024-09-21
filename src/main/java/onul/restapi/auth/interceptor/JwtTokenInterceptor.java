package onul.restapi.auth.interceptor;

import onul.restapi.common.AuthConstants;
import onul.restapi.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.rmi.RemoteException;

@Component
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final TokenUtils tokenUtils;

    public JwtTokenInterceptor(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        String token = tokenUtils.splitHeader(header);  // 인스턴스 메서드 호출

        if (token != null) {
            if (tokenUtils.isValidToken(token)) {  // 인스턴스 메서드 호출
                return true;
            } else {
                throw new RemoteException("Token이 만료되었습니다.");
            }
        } else {
            throw new RemoteException("Token 정보가 없습니다.");
        }
    }
}
