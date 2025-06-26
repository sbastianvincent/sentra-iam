package com.svincent7.sentraiam.identity.filter;

import com.svincent7.sentraiam.common.auth.AuthHeaderConstants;
import com.svincent7.sentraiam.common.auth.SentraPrincipal;
import com.svincent7.sentraiam.common.auth.endpoint.EndpointRuleProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

        String userId = request.getHeader(AuthHeaderConstants.USER_ID_HEADER);
        String username = request.getHeader(AuthHeaderConstants.USERNAME_HEADER);
        String tenantId = request.getHeader(AuthHeaderConstants.TENANT_ID_HEADER);
        String roles = request.getHeader(AuthHeaderConstants.ROLES_HEADER);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(username)
                || StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(roles)) {
            filterChain.doFilter(request, response);
            return;
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String auth : roles.split(",")) {
            authorities.add(new SimpleGrantedAuthority(EndpointRuleProvider.SCOPE_PREFIX + auth));
        }

        Authentication authentication = new SentraPrincipal(tenantId, userId, username, true, authorities);
        log.debug("AuthenticationFilter::authentication: {}", authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
