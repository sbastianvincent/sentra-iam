package com.svincent7.sentraiam.common.auth.endpoint;

import org.springframework.http.HttpMethod;

public record EndpointRule(HttpMethod method, String path, String authority) {

}

