package onul.restapi.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// cors 허용하기
public class HeaderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin","*"); // 다른 외부 요청을 허용할 것인가(CORS)
        res.setHeader("Access-Control-Allow-Methods","GET, HEAD, OPTIONS, POST, PUT, DELETE"); // 외부 허용할 메소드 종류
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers",
                "Origin, Access-Control-Allow-Origin, Access-Control-Allow-Headers, X-Requested-With, Content-Type, Authorization, X-XSRF-token"
        );
        res.setHeader("Access-Control-Allow-Credentials","true");

        filterChain.doFilter(request,response);
    }
}
