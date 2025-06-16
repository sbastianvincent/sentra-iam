package com.svincent7.sentraiam.common.filter;

import com.svincent7.sentraiam.common.logger.CachedHttpServletRequest;
import com.svincent7.sentraiam.common.logger.CachedLoggerConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Slf4j
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Getter
public class RequestCachingFilter extends OncePerRequestFilter {

    private final Set<String> excludedRequestPath;

    public RequestCachingFilter() {
        this(CachedLoggerConstant.EXCLUDED_RESPONSE_PATHS);
    }

    public RequestCachingFilter(final Set<String> excludedRequestPathInjection) {
        this.excludedRequestPath = excludedRequestPathInjection;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse,
                                    final FilterChain filterChain) throws ServletException, IOException {

        // Skip logging for this endpoint
        if (excludedRequestPath.contains(servletRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(servletRequest);
        log.info(CachedLoggerConstant.REQUEST_PREFIX + IOUtils.toString(
                cachedHttpServletRequest.getInputStream(), StandardCharsets.UTF_8));
        filterChain.doFilter(cachedHttpServletRequest, servletResponse);
    }
}
