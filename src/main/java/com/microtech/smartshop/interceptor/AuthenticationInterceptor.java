package com.microtech.smartshop.interceptor;

import com.microtech.smartshop.entity.User;
import com.microtech.smartshop.enums.UserRole;
import com.microtech.smartshop.exception.ForbiddenException;
import com.microtech.smartshop.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor for handling authentication and authorization
 * Checks HTTP session and user roles
 */
@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String USER_SESSION_KEY = "CURRENT_USER";

    /**
     * Public endpoints (no authentication required)
     */
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/swagger-ui",
            "/v3/api-docs",
            "/api-docs"
    };

    /**
     * Endpoints restricted to ADMIN only
     */
    private static final String[] ADMIN_ONLY_ENDPOINTS = {
            "/api/customers/.*",
            "/api/products/.*",
            "/api/orders/.*",
            "/api/payments/.*",
            "/api/promo-codes/.*"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // Ignore non-controller requests (static resources, etc.)
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String requestPath = request.getRequestURI();
        String method = request.getMethod();

        log.debug("Intercepting request: {} {}", method, requestPath);

        // 1. Check if this is a public endpoint
        if (isPublicEndpoint(requestPath)) {
            log.debug("Public endpoint, allowing access");
            return true;
        }

        // 2. Retrieve user from session
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.warn("No session found for request: {}", requestPath);
            throw new UnauthorizedException("Authentication required. Please login first.");
        }

        User currentUser = (User) session.getAttribute(USER_SESSION_KEY);
        if (currentUser == null) {
            log.warn("No user in session for request: {}", requestPath);
            throw new UnauthorizedException("Authentication required. Please login first.");
        }

        log.debug("Authenticated user: {} (role: {})", currentUser.getUsername(), currentUser.getRole());

        // 3. Check permissions for ADMIN-only endpoints
        if (isAdminOnlyEndpoint(requestPath) && currentUser.getRole() != UserRole.ADMIN) {
            log.warn("User {} (role: {}) attempted to access admin endpoint: {}",
                    currentUser.getUsername(), currentUser.getRole(), requestPath);
            throw new ForbiddenException("Access denied. Admin privileges required.");
        }

        // 4. Handle CLIENT endpoints
        if (requestPath.startsWith("/api/customers/me")) {
            // Clients can access only their own data
            if (currentUser.getRole() != UserRole.CLIENT && currentUser.getRole() != UserRole.ADMIN) {
                throw new ForbiddenException("Access denied.");
            }
        }

        // 5. Store user into request attributes for use in controllers
        request.setAttribute("currentUser", currentUser);

        log.debug("Access granted for user: {}", currentUser.getUsername());
        return true;
    }

    /**
     * Checks if the endpoint is public
     */
    private boolean isPublicEndpoint(String path) {
        for (String publicPath : PUBLIC_ENDPOINTS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the endpoint is restricted to ADMIN
     */
    private boolean isAdminOnlyEndpoint(String path) {
        for (String adminPath : ADMIN_ONLY_ENDPOINTS) {
            if (path.matches(adminPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        // Cleanup if necessary
        request.removeAttribute("currentUser");
    }
}
