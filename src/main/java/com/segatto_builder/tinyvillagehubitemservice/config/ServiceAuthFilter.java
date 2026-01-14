package com.segatto_builder.tinyvillagehubitemservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class ServiceAuthFilter extends OncePerRequestFilter {

    @Value("${service.auth.key}")
    private String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String clientIp = getClientIp(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String serviceKey = request.getHeader("X-Service-Key");
        
        if (serviceKey == null || !serviceKey.equals(key)) {
            log.warn("BLOCKED - Invalid service key from IP: {}, Method: {}, URI: {}", clientIp, method, uri);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid secret key\"}");
            return;
        }
        log.debug("ALLOWED - Valid service key from IP: {}, Method: {}, URI: {}", clientIp, method, uri);
        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        // Check for proxy headers first (for Docker/load balancer scenarios)
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For can contain multiple IPs, get the first one
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
