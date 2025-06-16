package com.svincent7.sentraiam.auth.filter;

import com.svincent7.sentraiam.common.filter.RequestCachingFilter;
import com.svincent7.sentraiam.common.filter.ResponseCachingFilter;
import com.svincent7.sentraiam.common.logger.CachedLoggerConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Component
public class LogFilterConfig {
    private static final int MAX_PAYLOAD_LENGTH = 10000;

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(MAX_PAYLOAD_LENGTH);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix(CachedLoggerConstant.REQUEST_PREFIX);
        return filter;
    }

    @Bean
    public RequestCachingFilter requestCachingFilter() {
        return new RequestCachingFilter();
    }

    @Bean
    public ResponseCachingFilter responseCachingFilter() {
        return new ResponseCachingFilter();
    }
}
