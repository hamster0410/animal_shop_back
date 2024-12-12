package animal_shop.global.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
        final String accessToken = parseBearerToken(request);
        final String refreshToken = parseRefreshToken(request);

        log.info("filter is running...");

        if (accessToken != null && !accessToken.equalsIgnoreCase("null")) {
            // Token validation
            String userId = tokenProvider.validateAndGetUserId(accessToken);
            String role = tokenProvider.getRoleFromToken(accessToken);
            log.info("Authenticated user ID: " + userId + ", Role: " + role);

            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + role);

            // Authentication setup
            AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }// Access Token이 만료되었을 때 Refresh Token으로 재인증 시도
//        else if (refreshToken != null && !refreshToken.equalsIgnoreCase("null")) {
//            if (tokenProvider.validateRefreshToken(refreshToken)) {
//                String userId = tokenProvider.getUserIdFromRefreshToken(refreshToken);
//                log.info("Authenticated user ID using Refresh Token: " + userId);
//
//                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//                securityContext.setAuthentication(authentication);
//                SecurityContextHolder.setContext(securityContext);
//            } else {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Refresh Token");
//                return;
//            }
//        }
    } catch (ExpiredJwtException e) {
        logger.error("Token has expired", e);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
        return; // Exit the filter chain
    } catch (Exception e) {
        logger.error("Could not set user authentication in security context", e);
    }
    filterChain.doFilter(request, response);
}


    private String parseBearerToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    private String parseRefreshToken(HttpServletRequest request) {
        return request.getHeader("Refresh-Token"); // Refresh Token은 Refresh-Token 헤더에서 직접 추출
    }
}
