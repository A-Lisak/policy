# policy

Motor Policy Service

## Ports

| Port | Description |
| --- | --- |
| `9100` | Main rest application port |

## Links

| Environment   | URI                                                                                                                  |
|---------------|----------------------------------------------------------------------------------------------------------------------|
| Localhost     | http://localhost:9100/policy/v2/                                |
| Kong(deve) | https://ops-kong-deve13.escloud.co.uk/policy/v2/  |
| Beanstalk(deve) | https://policy-deve.eu-west-1.elasticbeanstalk.com/policy/v2/ |
| Swagger(deve) | https://policy-deve.eu-west-1.elasticbeanstalk.com//policy/v2/swagger-ui.html |
| Documentation | https://s3-eu-west-1.amazonaws.com/s3-ec-ext-dev-all-maven.rpt-01/policy/index.html |
| Spring boot admin (dev) | https://web-jva-springbootadmin-dev.escloud.co.uk |

## Operations

### retrieve (GET /motor-policies/{policyNumber}?version={version})

# Service Dependencies
The service needs to connect to the following services:
- Oracle Gateway API (Github project api-jva-oraclegateway). 

# Environment Variables
The following values can be declared as environment variables. The one marked required **must** be declared.

| Variable Name       | Required                      | Description                                  | Value                  |
|---------------------|:-----------------------------:|----------------------------------------------|------------------------|
| ENV                 | **true**                      | The name of the environment                  | deve, tste, prep, prod |
| MODE                | **true**                      | The mode to operate in                       | dev, test, prep, prod  |
| APP_PORT            | **true**                      | Application Port                             | 80  |
| CONFIG_FAIL_FAST_FLAG|**true**                      | Whether startup should fail if cannot connect to config server | true |
| CONFIG_GIT_BRANCH   | **true**                      | Cloud config label indicating Git branch     | master |
| CONFIG_SERVER_URL   | **true**                      | Cloud config label indicating Git Server URL | https://api-jva-springbootconfig-{dev}.escloud.co.uk |
| ORACLE_SERVICE_TIMEOUT | **false**                  | Document Access Service Timeout milliseconds | 5000  |
| API_KEY             | **true**                      | API key (header) used by the service         | XXXXX  |
| DOMAIN              | **true**                      | Kong gateway domain                          | escloud.co.uk  |
| ROOT_LOGGING_LEVEL  | **false**                     | Default log level                            | WARN  |
| ku_LOGGING_LEVEL | **false**                     | ku packages log level                     | INFO  |
| APP_VERSION         | **true**                      | Application version running                  | 2  |
| API_GATEWAY_HOST    | **true**                      | Base URL of api gateway                      | https://ops-kong-{deve13}.escloud.co.uk |
| LOGS_PATH           | **false**                     | Application logs directory                   | /application/logs |

# Security
For authentication we use API key in Kong so we need to pass **apikey** header between our services and also the JWT coming from the UI.
Because of that we define an API_KEY environment variable got from AWS System Manager (SSM) and it's stored on KMS.

Check -entrypoint.sh- script to understand how is the process to get APY_KEY and set the environment variable.

Api key and values are in the bootstrap.yml file but the key comes from the environment variable.
```
api-key:
  name: apikey
  secret: ${API_KEY}
```

 **Note:** Be aware key property name is important because spring boot admin hides properties/environment variables end on "password", "secret" or "key"
 
## Logging
This service uses Logback and Logbook libraries to log application messages and resquests/responses respectively.

### Logback
You can find logback configuration in **logback-spring.xml** there you can find two appenders:
 * **ASYNC-APP** will write application logs in console asynchronously.
 * **ASYNC-REQUEST** will write requests and responses logs in request.log file asynchronously and the path is specified in *logging.path* bootstrap property.

You can find more information about logback here : https://logback.qos.ch/documentation.html

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
You can find more information about logbook here : https://github.com/zalando/logbook

### AWS environments
Logs are copied to CloudWath. Here you have all the details a bout CloudWatch logs:
 * **CloudWatch LogGroup:** Beanstalk Application name (i.e. policy-deve)
 * **Streams in CloudWatch:** 
    * *request-{instance-id}:* It contains request and responses processed by the service
    * *application-{instance-id}:* It contains application logs
    * *ecs-agent-{instance-id}:* Log messages from the Amazon ECS container agent. You can find information when new tasks are deployed.
    * *ecs-init-{instance-id}:* ECS instance startup logs.
    * *eb-activity-{instance-id}:* Beanstalk logs.
    * *eb-ecs-mgr-{instance-id}:* Beanstalk and ECS logs. 
    * *messages-{instance-id}:* Global system messages.
    * *dmesg-{instance-id}:* The message buffer of the Linux kernel.
    * *docker-event-{instance-id}:* Docker event logs.


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


