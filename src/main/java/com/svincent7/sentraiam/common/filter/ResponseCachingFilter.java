package com.svincent7.sentraiam.common.filter;

import com.svincent7.sentraiam.common.logger.CachedHttpServletResponse;
import com.svincent7.sentraiam.common.logger.CachedLoggerConstant;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Slf4j
@Getter
public class ResponseCachingFilter implements Filter {

    private final Set<String> excludedResponsePath;

    public ResponseCachingFilter() {
        this(CachedLoggerConstant.EXCLUDED_RESPONSE_PATHS);
    }

    public ResponseCachingFilter(final Set<String> excludedRequestPath) {
        this.excludedResponsePath = excludedRequestPath;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain filterChain)
            throws IOException, ServletException {
        if (servletResponse.getCharacterEncoding() == null) {
            servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        }

        // Skip logging for this endpoint
        String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();
        if (excludedResponsePath.contains(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        CachedHttpServletResponse cachedHttpServletResponse = new CachedHttpServletResponse(
                (HttpServletResponse) servletResponse);

        try {
            filterChain.doFilter(servletRequest, cachedHttpServletResponse);
            cachedHttpServletResponse.flushBuffer();
        } finally {
            byte[] copy = cachedHttpServletResponse.getCached();
            log.info(CachedLoggerConstant.RESPONSE_PREFIX + new String(copy, servletResponse.getCharacterEncoding()));
        }
    }
}
