This section covers the example of 
1. we use the gateway server
3. update docker compose

Using gateway server we have implemented routing, prefilter and post filters. the prefilters and postfilters have helped in tracing and logging

API gateway handles the cross cutting concerns in an application such as logging, auditing, tracing and security. It serves as an entry point to all the incoming request from client and the client does not need to keep track of services involved in a transaction.

It also handles dynamic routing.

Spring cloud gateway dependecny can be used to add the gateway in your microservices network. It acts as a gatekeeper to the entire microservices


management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    gateway:
      enable: true  // This will enable the gateway related endpoints
	  

spring:	  
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true// this will connect with discovery server and locate all the details related to microservices.
		  
		  
		  
		  
		  
gateway server
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

http://192.168.29.26:8072/actuator/gateway/routes


To hit the application through gateway we need to hit url as below, the gateway uses logical name of microservices to route the request
http://localhost:8072/ACCOUNTS/api/create : by default the eureka has name in CAPS for registered service

For Lower case-----------> http://localhost:8072/accounts/api/create
spring: 
 cloud:
    gateway:
      discovery:
        locator:
          enabled: false// with tis value as false the default URLs for service are disbaled 
          lowerCaseServiceId: true// need to set the property


http://localhost(Ip gatewayserver):port( gateway server)/ logical name of microservice/url for service method

when we want to modify the URL in a custom way like 
http://localhost:8072/accounts/api/create -------------> http://localhost:8072/eazybank/accounts/api/create

we need to create a bean for RouteLocator, 
@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder){
	return routeLocatorBuilder.routes()
			.route(p->p.path("/eazybank/accounts/**") /// when there is any reuqest with this path pattern
				.filters(f->f.rewritePath("/eazybank/accounts/(?<segment>.*)","/${segment}")) // rewrite this path pattern with segment to only segment
				.uri("lb://ACCOUNTS"))// with loadbalancing ACCOUNTS service registered on eureka -lb indicates client side load balancing with spring cloud //load balancer http://localhost:8072/eazybank/accounts/api/create ---> will be rewritten as http://localhost:8072/accounts/api/create //internally and request will be routed accordingly 
			.route(p->p.path("/eazybank/loans/**")
					.filters(f->f.rewritePath("/eazybank/loans/(?<segment>.*)","/${segment}"))
					.uri("lb://LOANS"))
			.route(p->p.path("/eazybank/cards/**")
					.filters(f->f.rewritePath("/eazybank/cards/(?<segment>.*)","/${segment}"))
					.uri("lb://CARDS")).build();

	}
	
	.route(p -> p
						.path("/eazybank/accounts/**")
						.filters( f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))// This filter will add the response header with 
								// name X-Response-Time and value as time
						.uri("lb://ACCOUNTS"))
						
						
Tracing and Logging: when the request travels through multiple services, we can add a correlation id for tracing at the begenning and same can further logged for identification purpose. The same correlation id can also be used in reponse header for further auditing purpose of incoming request.
For this logic implemenation we can use custom filter inside gateway server .These custom filter can implementing global filter

Mono in Reactive : Single object
Flux in Reactive : Multiple object

Request Filter----
 @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (isCorrelationIdPresent(requestHeaders)) { // check if correaltion id is present
            logger.debug("eazyBank-correlation-id found in RequestTraceFilter : {}", //log
                    filterUtility.getCorrelationId(requestHeaders));
        } else {
            String correlationID = generateCorrelationId(); // add the correaltion id
            exchange = filterUtility.setCorrelationId(exchange, correlationID);
            logger.debug("eazyBank-correlation-id generated in RequestTraceFilter : {}", correlationID); //log
        }
        return chain.filter(exchange); // This will not return anything and will only chain the filter on excahnge
    }
	
Response Filter----

@Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> { // It is the same exchnage and chain as was in request
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { // only execeuted when request sent and response recevied by using 'then'
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String correlationId = filterUtility.getCorrelationId(requestHeaders);
                logger.debug("Updated the correlation id to the outbound headers: {}", correlationId); //log
                exchange.getResponse().getHeaders().add(filterUtility.CORRELATION_ID, correlationId);  // add the correlation id in response header
            }));
        };
    }
	
logging:
  level:
    com:
      eazybytes:
        gatewayserver: DEBUG // enbale looging bys adding this property in yaml mentioned root package path
		
		
when the gateway will route the request to applicable service the header will be present with the name of correlationId
public ResponseEntity<CustomerDetailDTO> fetchCustomerDetails(
            @RequestHeader("eazybank-correlation-id") String correlationId,// Request Header value
            @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber)
			
Also for docker-compose the gateway server will need the info of accounts/loans/cards services health check while routing the request so we need to add the same in application.yaml for these services.

management:
 health:
  readiness-state: 
   enabled: true
  liveness-state:
   enabled: true
 endpoint:
  health:
   probes:
    enabled: true
	
	
for docker compose file we will add heathcheck property inside each cards/loans/account ms



  cards:
    image: "elearn3/cards:s9"
    container_name: cards-ms
    ports:
      - "9000:9000"
    depends_on:
      configserver:
        condition: service_healthy
      eurekaserver:
        condition: service_healthy
    healthcheck:
      test: "curl --fail --silent localhost:9000/actuator/health/readiness | grep UP || exit 1"
      interval: 10s
      timeout: 5s
      retries: 10
      start_period: 10s
    environment:
      SPRING_APPLICATION_NAME: "cards"
    extends:
      file: common-config.yaml
      service: microservice-eureka-config

docker compose gateway server changes 


  gatewayserver:
    image: "elearn3/gatewayserver:s9"
    container_name: gatewayserver-ms
    ports:
      - "8072:8072"
    depends_on:
      configserver:
        condition: service_healthy
      eurekaserver:
        condition: service_healthy
      accounts:
        condition: service_healthy
      cards:
        condition: service_healthy
      loans:
        condition: service_healthy
    environment:
      SPRING_APPLICATION_NAME: "gatewayserver"
    extends:
      file: common-config.yaml
      service: microservice-eureka-config