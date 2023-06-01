package com.ordermanagement.apigateway.filter;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    /**
     *  if request pattern is like anything from openApiEndPoints, we will ignore.
    In Api gateway we will only validate the token. It doesn't make sense to force the user to validate the
    token while getting the token
     **/
    public static final List<String> openApiEndPoints = List.of(
            "/auth/register",
            "/auth/token",
            "/eureka"
            );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndPoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
