package com.library;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthService auth;
    public WebConfig(AuthService auth) { this.auth = auth; }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object h) throws Exception {
                if ("OPTIONS".equals(req.getMethod())) return true;
                String header = req.getHeader("Authorization");
                String token = header != null && header.startsWith("Bearer ") ? header.substring(7) : null;
                if (!auth.valid(token)) {
                    res.setStatus(401);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\":\"Login required\"}");
                    return false;
                }
                return true;
            }
        }).addPathPatterns("/api/admin/**");
    }
}
