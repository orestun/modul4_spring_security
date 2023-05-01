package com.epam.esm.feignClient;

import feign.Headers;
import feign.QueryMap;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@PropertySource("classpath: application.properties")
@FeignClient(value = "${feign.client.config.name}", url = "${feign.client.config.url}")
public interface CustomFeignClient {
    @RequestLine("GET")
    @Headers("Content-Type: application/json")
    Map<String, String> verifyTokenAndGetMapOfClaims(@QueryMap Map<String, String> queryParams);
}
