# URL Shortener
The URL Shortener project is a web application developed using Java, Spring Boot, and MongoDB. 
Its main purpose is to provide a service that allows users to shorten long URLs into shorter, more manageable links. 
This project aims to simplify the sharing and tracking of URLs, making them easier to remember and share across various platforms.
### Key Features:
- **URL Shortening:** Users can input long URLs and generate shortened links that are easier to share and remember.
- **Expiration and Tracking:** Shortened URLs can be set with an expiration date, after which they will no longer be accessible. Additionally, the system tracks the number of visits and provides analytics for each shortened URL.
- **Duplicate URL Handling:** The system checks for duplicate URLs and ensures that multiple shortcodes are not generated for the same URL.
- **Error Handling:** The project includes robust error handling mechanisms to handle various scenarios, such as shortcode collisions.

[//]: # (Key Features:)
[//]: # (URL Shortening: Users can input long URLs and generate shortened links that are easier to share and remember.)
[//]: # (Custom Shortcodes: Users have the option to customize the generated shortcodes for their URLs, making them more personalized and meaningful.)
[//]: # (Expiration and Tracking: Shortened URLs can be set with an expiration date, after which they will no longer be accessible. Additionally, the system tracks the number of visits and provides analytics for each shortened URL.)
[//]: # (Duplicate URL Handling: The system checks for duplicate URLs and ensures that multiple shortcodes are not generated for the same URL.)
[//]: # (Error Handling: The project includes robust error handling mechanisms to handle various scenarios, such as shortcode collisions and invalid URLs.)

## Feature Roadmap 🛣️
### URL Shortener Service
- [x] Setup call to metrics-service via Kafka every time the URL is visited
- [ ] Handle invalid URLs
- [ ] Custom short codes
- [ ] Activate deactivated shortcode if new add-url-request comes for existing deactivated url
### DB Service
- [x] Setup endpoint to process add-metrics
- [x] Add logic to save URL metrics to DB
### Metrics Service
- [x] Setup metrics service
- [x] Setup Kafka service to consume add-metrics messages from url-shortener-service
- [x] Setup Feign client calling DB service
- [x] Setup metrics logic for URL visit
### Future
- [ ] Add swagger
- [ ] Enable caching
- [ ] Implement service discovery and load balancing
- [ ] Implement circuit breaker using Resilience4J
- [ ] Implement API security

## Kafka Installation using Brew on MacOS
Run this command in terminal:
```shell
$ brew install kafka
```
This will install Zookeeper and then Kafka. To start Kafka, Zookeeper must be running. 
Run below commands:
```shell
$ brew services start zookeeper
$ brew services start kafka
```
To stop the services, run below commands:
```shell
$ brew services stop kafka
$ brew services stop zookeeper
```