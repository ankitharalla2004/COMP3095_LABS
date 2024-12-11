package ca.gbc.apigateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;


@Configuration
@Slf4j
public class Routes {
    @Value("${service.product-url}")
    private String productServiceUrl;

    @Value("${service.order-url}")
    private String orderServiceUrl;

    @Value("${service.inventory-url}")
    private String inventoryServiceUrl;


    @Bean
    public RouterFunction<ServerResponse> productServiceRoutes(){
        log.info("Initializing product-service routes with URL : {}",productServiceUrl);
        return route("product-service")
                .route(RequestPredicates.path("/api/product"),request->{
                    log.info("Received request for product-service: {}",request.uri());
                    return HandlerFunctions.http(productServiceUrl).handle(request);
                })
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("productServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoutes(){
        log.info("Initializing order-service routes with URL : {}",orderServiceUrl);
        return route("order-service")
                .route(RequestPredicates.path("/api/order"),request->{
                    log.info("Received request for order-service: {}",request.uri());

                    try {
                        ServerResponse response = HandlerFunctions.http(orderServiceUrl).handle(request);
                        log.info("Response status: {}",response.statusCode());
                        return  response;
                    }
                    catch (Exception e){
                        log.error("Error occurred while routing to: {}", e.getMessage(),e);
                        return ServerResponse.status(500).body("Error occurred when routing to order-service");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoutes(){
        log.info("Initializing inventory-service routes with URL : {}",inventoryServiceUrl);
        return route("inventory-service")
                .route(RequestPredicates.path("/api/inventory"),request->{
                    log.info("Received request for inventory-service: {}",request.uri());

                    try {
                        ServerResponse response = HandlerFunctions.http(inventoryServiceUrl).handle(request);
                        log.info("Response status: {}",response.statusCode());
                        return  response;
                    }
                    catch (Exception e){
                        log.error("Error occurred while routing to: {}", e.getMessage(),e);
                        return ServerResponse.status(500).body("Error occurred when routing to inventory-service");
                    }
                })
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute(){
        return route("product-service-swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
                        HandlerFunctions.http(productServiceUrl))
                .filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("ProductSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute(){
        return route("order-service-swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                        HandlerFunctions.http(orderServiceUrl))
                .filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("OrderSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute(){
        return route("inventory-service-swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
                        HandlerFunctions.http(inventoryServiceUrl))
                .filter(setPath("/api-docs"))
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("InventorySwaggerCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute(){

        return route("fallbackRoute")
                .route(RequestPredicates.all(),
                        request ->ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Service is Temporarily Unavailable, please try again later"))
                .build();
    }

}