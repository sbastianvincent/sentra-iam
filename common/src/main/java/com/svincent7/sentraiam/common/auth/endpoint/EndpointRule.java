package com.svincent7.sentraiam.common.auth.endpoint;

import org.springframework.http.HttpMethod;

import java.util.Set;

public record EndpointRule(HttpMethod method, String path, Set<String> authority) {

}

