package com.ordermanagement.apigateway.filter;

import com.ordermanagement.apigateway.exception.HeaderDoesNotContainJwtTokenException;
import com.ordermanagement.apigateway.exception.UnAuthorizedException;
import com.ordermanagement.apigateway.util.JwtUtil;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * This class will check the header, if it contains token. If yes it will validate the token.
 * So before accessing any service this filter will verify the jwt header.
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter(){
        super(Config.class);
    }

    // if request is not part of openApiEndPoints list, then only apply the filter and do authentication
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())){
                //if header contains the token or not
                if(!isHeaderContainsToken(exchange))
                    throw new HeaderDoesNotContainJwtTokenException("Header doesn't contain Jwt Token");
            }
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if(null != authHeader && authHeader.startsWith("Bearer "))
                authHeader = authHeader.substring(7);
            //Now I have token with me, will validate the token
            try{
                jwtUtil.validateToken(authHeader);
            }
            catch (Exception e){
                throw new UnAuthorizedException("User is not authorized to access.");
            }
            return chain.filter(exchange);
        });
    }

    private boolean isHeaderContainsToken(ServerWebExchange exchange){
        return exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }
    public static class Config{

    }
}
