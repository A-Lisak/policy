# policy

Motor Policy Service

## Ports

| Port | Description |
| --- | --- |
| `9100` | Main rest application port |

## Links

### Logbook
This zalando library allows logging requests and responses in a secure and configurable way. You can setup logbook in bootstrap.yml
```
 logbook:
   format.style: json
   obfuscate:
     headers:
       - apikey
       - JWT
   write:
     level: DEBUG
     category: request-response
```

## Monitoring

For development and test this application connects to the Spring Boot Cloud admin service

## Usage

### Build the application
```
    mvn clean install
```
### Run tests
```
    mvn clean test
```
### Run component tests
```
    mvn clean integration-test
```
### Run application
```
    java -jar target/policy.jar
```
### Generate site with documentation and reports
```
    mvn site
```


