package com.svincent7.sentraiam.identity.filter;

import com.svincent7.sentraiam.identity.auth.SentraPrincipal;
import com.svincent7.sentraiam.identity.auth.endpoint.EndpointRuleProvider;
import com.svincent7.sentraiam.identity.dto.credential.TokenConstant;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Component
@AllArgsConstructor
@Order(1)
public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        log.debug("AuthenticationFilter::doFilterInternal: {}", request.getRequestURI());

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                var auth = SecurityContextHolder.getContext().getAuthentication();

                if (auth instanceof JwtAuthenticationToken jwtAuth) {
                    String userId = jwtAuth.getName();
                    String tenantId = jwtAuth.getToken().getClaim(TokenConstant.TENANT_ID);
                    String username = jwtAuth.getToken().getClaim(TokenConstant.USERNAME);
                    String roles = String.join(",", jwtAuth.getToken().getClaimAsStringList(TokenConstant.SCOPES));
                    if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(username)
                            || StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(roles)) {
                        filterChain.doFilter(request, response);
                        return;
                    }

                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    for (String role : roles.split(",")) {
                        authorities.add(new SimpleGrantedAuthority(EndpointRuleProvider.SCOPE_PREFIX + role));
                    }

                    Authentication authentication = new SentraPrincipal(tenantId, userId, username, true, authorities);
                    log.debug("AuthenticationFilter::authentication: {}", authentication);

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
