## SpringBoot & Elasticsearch Proof-of-Concept
A small application to showcase the integration between Spring Data and Elasticsearch.

Start the Docker containers:
```
docker-compose up
```
Run the application (requires Java 23):
```
mvn spring-boot:run
```
Use Kibana to inspect the index mappings created automatically by Spring Data.

Prometheus is used for metrics storage.

The Grafana dashboard was taken from https://grafana.com/grafana/dashboards/19004-spring-boot-statistics/


## Endpoints
### Swagger
http://localhost:8080/swagger-ui/index.html

### Kibana
http://localhost:5601/app/home#/

### Spring Actuator (to view e.g. metrics)
http://localhost:8080/actuator

### Grafana
http://localhost:3000/

Login: admin/admin

### Prometheus
http://localhost:9090/