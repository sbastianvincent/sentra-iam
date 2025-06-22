package com.svincent7.sentraiam.pki.filter;

import com.svincent7.sentraiam.common.exception.UnauthorizedException;
import com.svincent7.sentraiam.pki.config.PkiConfig;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class AuthenticationFilter extends OncePerRequestFilter {

    private final PkiConfig config;

    private static final String HEADER_AUTHENTICATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    // TEMPORARY UNTIL TEMP_SERVICE_ACCOUNT_TOKEN is UPDATED
    private static final List<String> LIST_URL_WITHOUT_AUTHENTICATION = List.of();

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        log.debug("AuthenticationFilter::doFilterInternal: {}", request.getRequestURI());
        if (LIST_URL_WITHOUT_AUTHENTICATION.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String headerAuth = request.getHeader(HEADER_AUTHENTICATION);
        if (StringUtils.isEmpty(headerAuth) || !headerAuth.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException("Authorization header is empty");
        }

        String token = headerAuth.substring(BEARER_PREFIX.length());
        log.debug("AuthenticationFilter::token: {}", token);

        if (!token.equals(config.getBootstrapCaToken())) {
            throw new UnauthorizedException("Invalid Token");
        }

        filterChain.doFilter(request, response);
    }
}
