# URL Shortener
It's a URL shortener application developed using Java, Spring Boot and MongoDB.

## Feature Roadmap

### URL Shortener Service
- [ ] Setup call to metrics-service every time the URL is visited
- [ ] Setup Feign client calling metrics-service
### DB Service
- [ ] Setup endpoint to process add-metrics
- [ ] Add logic to save URL metrics to DB
### Metrics Service
- [x] Setup metrics service
- [ ] Setup Feign client calling DB service
- [x] Setup Kafka service to consume add-metrics messages from url-shortener-service
- [ ] Setup metrics logic for URL visit

### Future
- [ ] Add swagger
- [ ] Enable caching
- [ ] Implement service discovery and load balancing
- [ ] Implement circuit breaker using Resilience4J
- [ ] Implement API security
