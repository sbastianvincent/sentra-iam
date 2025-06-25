package com.svincent7.sentraiam.identity.filter;

import com.svincent7.sentraiam.common.auth.SentraPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@Order(1)
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHENTICATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    // TEMPORARY UNTIL TEMP_SERVICE_ACCOUNT_TOKEN is UPDATED
    private static final List<String> LIST_URL_WITHOUT_AUTHENTICATION = List.of(
    );

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        log.debug("AuthenticationFilter::doFilterInternal: {}", request.getRequestURI());
        if (LIST_URL_WITHOUT_AUTHENTICATION.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            List<String> valueList = Collections.list(values);
            log.debug("AuthenticationFilter::header '{}': {}", name, valueList);
        }

        String headerAuth = request.getHeader(HEADER_AUTHENTICATION);
        if (StringUtils.isEmpty(headerAuth) || !headerAuth.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = headerAuth.substring(BEARER_PREFIX.length());
        Authentication authentication = SentraPrincipal.fromToken(token);
        log.debug("AuthenticationFilter::authentication: {}", authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
