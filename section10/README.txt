This section covers the example of 
1. we use use different startegy of rsellience4j pattern inside our gaetway server.
2. circuit breaker, fallback, retry ,rate limiter
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
</dependency>
		
		
-----------Circuit Breaker----------		
resilience4j.circuitbreaker:
  configs:
    default:
      slidingWindowSize: 10 // before moving to close/open status monitor this many request
      permittedNumberOfCallsInHalfOpenState: 2 // to allow number of request when in half opens state, based on these request it can move to open or close
      failureRateThreshold: 50//If atleast 50% of sliding window fails it moves to open state
      waitDurationInOpenState: 10000//ms CB wait for 10 s , whenever try to move to half open state and allow partial traffic
	  
	 *****if the count slidingWindowSize size is 10 and failure threshold is %50, when circuit breaker detect 5 failure out of last 10 calls, it changes from CLOSED to OPEN.
	  
Route.
public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p
						.path("/eazybank/accounts/**")
						.filters( f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountsCircuitBreaker")))// Added circuitBreaker
						.uri("lb://ACCOUNTS"))
								
								
								
------------Retry------------
1. with gateway for loans
	.route(p -> p
						.path("/eazybank/loans/**")
						.filters( f -> f.rewritePath("/eazybank/loans/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true)))
						.uri("lb://LOANS"))
						
						
						
2.retry inside with accounts  microservie 
@Retry(name="getBuildInfo",fallbackMethod = "getBuildIfoFallback")
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo() {
		return ResponseEntity.status(HttpStatus.OK).body(buildVerion);
	}

	public ResponseEntity<String> getBuildIfoFallback(Throwable throwable) {
		return ResponseEntity.status(HttpStatus.OK).body("0.9");
	}
	

resilience4j.retry:
  configs:
    default:
      maxRetryAttempts: 3
      waitDuration: 500
      enableExponentialBackoff: true
      exponentialBackoffMultiplier: 2
      ignoreExceptions://Ignore this, redundant with retryExceptions
        - java.lang.NullPointerException
		  retryExceptions:// Only try for this, rest all will be ignored
			- java.util.concurrent.TimeoutException'
			
@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}			
			
-----------------rate limiter with cards service
Chnges inside gateway eerver
1. depdency added
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>

2. Bean added
	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(1, 1, 1);
	}
	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
				.defaultIfEmpty("anonymous");
	}
3. Routing config modified
.route(p -> p
			.path("/eazybank/cards/**")
			.filters( f -> f.rewritePath("/eazybank/cards/(?<segment>.*)","/${segment}")
					.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
					.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
							.setKeyResolver(userKeyResolver())))
			.uri("lb://CARDS")).build();
			
spring:
 data:
    redis:
      connect-timeout: 2s
      host: localhost
      port: 6379
      timeout: 1s
	  
4. need to install redis 
	docker run -p 6379:6379 --name eazyredis -d redis
	
	
	
for multiple request we may need apache benchmark
5. ab -n 10 -c 2 -v 3 http://localhost:8072/eazybank/accounts/api/java-version

----rate limiter pattern inside accounts microservice
resilience4j.ratelimiter:
  configs:
    default:
      timeoutDuration: 1000
      limitRefreshPeriod: 5000
      limitForPeriod: 1


@RateLimiter(name="getJavaVersion")
	@GetMapping("/java-version")
	public ResponseEntity<String> getEnvInfo() {
		return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
//		return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("MAVEN_HOME"));
	}
	
	
///Docker compose

redis:
    image: redis
    ports:
      - "6379:6379"
    healthcheck:
      test: [ "CMD-SHELL", "redis-cli ping | grep PONG" ]
      timeout: 10s
      retries: 10
    extends:
      file: common-config.yml
      service: network-deploy-service