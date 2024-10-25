## How to run
Start the docker containers (Elasticsearch, Kibana):
```
docker-compose up
```
Run the application (requires Java 23):
```
mvn spring-boot:run
```

The Grafana dashboard was taken from https://grafana.com/grafana/dashboards/19004-spring-boot-statistics/

Login: admin/admin

## Endpoints
Swagger: http://localhost:8080/swagger-ui/index.html

Kibana: http://localhost:5601/app/home#/

Spring Actuator (to view e.g. metrics): http://localhost:8080/actuator

Grafana: http://localhost:3000/

Prometheus: http://localhost:9090/