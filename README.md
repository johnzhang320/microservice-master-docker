# microservice-master
SpringBoot microservice 

## Environment

  Intellij Community Edition and more
  
  One parent project and 6 modules
  
  SpringBoot 2.7.8 
  
  Eureka Discovery Server
  
  Eureka Client
  
  Open Feign Client Interface
  
  Resilience4J Circuit Break
  
  Spring Cloud API Getway
  
  Confluent Kafka
  
  JDK 1.8 
  
  Rest API
  
  ModelMapper
  
  Global Exception Handler 
 
  Spring Lombok
  
  Mongodb 4.4.18
  
  mysql 8.0.28
  
  Docker
  
 
  
## Microservices and applied technologies

  microservice-master --- parent pom.xml hodling all 6 modules names, spring boot version, spring cloud version , dependencyManagement, build plgin 

  eureka-name-server ---- register, discovery and load balance for all microservices
  
  eureka-client-api-getway ---- supported by spring-cloud-starter-gateway to route to services, new replace of zuul
  
  OPEN FEIGN  ---- defining feign client interfaces to have services call other other, rew replace of Webclient of web-flux
  
  RESILIENCE4J ---- circuit breaker and fallback support, new replace of hystrix
  
  KAFKA EVENT DRIVEN ---- kafka producer and consumer Serialize/Deserialize Json Object via using confluent zookeeper and kafka docker-compose
  
  product-service ---- Eureka Client, create products in Mongodb,  authenticate mongo, configure MongoRepository, provide 5 rest APIs
  
  inventory-service ---- Eureka/Feign Client, create inventory in MySQL, productId, product name, price call product service by product 
  
  order-service ---- Eureka/Feign Client, Resilience4J, Kafka Producer, place order to MySQL, check inventory quantity, send event to notification
  
  notification-service ---- Kafka Consumer, listening Order-Service Order Event 
  
## Installation and Setup
  
### Mongodb Authentication
   
   1. Using mongodb download and install tool to initialize such as install to mac
   
      brew install mongodb
   
   2. start Mongodb without access control
      if mongo is running
      
        ~$ pgrep mongo
      
      to find process Id
      
        ~$ kill -9  process Id
      
      start without access
      
       ~$ mongod --dbpath /usr/local/var/mongodb
   
       ~$  mongo
      
     > use admin
     >  db.createUser(
     {
       user: "mongoadmin",
       pwd: "adminonly",
       roles: [ { role: "userAdminAnyDatabase", db: "admin" },
                { role: "readWrite", db: "admin" }
              ]
     })
   
   Ctrl-C quit mongodb, using pgrep mongo and kill command stop mongod and then type following common, 
   
   start mongod as authentication access
   
      ~$ mongod --auth --dbpath /usr/local/var/mongodb
    
   login as admin
   
      ~$ mongo --authenticationDatabase admin -u mongoadmin -p adminonly
   
   create 'product_services' document (database called in mysql), open a document which does not exist and insert one record 
   
     > use product_services
    
     > db.product_services.insert({"name":"product microservices"})

     > show dbs

   Then create username and password to access product_services
   
     > Db.createUser(
       {
          user: "productsuper",
          pwd: "super123",
         roles: [ { role: "readWrite", db: "product_services" } ]
       })
   
### Mongodb Configuration in application.yml
    spring:
      data:
        mongodb:
          host: localhost
          port: 27017
          database: product_services
          username: productsuper
          password: super123
          repositories:
            enabled: true
  
  in pom.xml
  
       <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
  
  ## MySQL Authentication and configuration
      
     Download and Install MySQL , using the install tool set up and choose authenticated start root user 
     
     password: mypassword
     
     mysql> create database inventory_service
     
     mysql> create database order_service
     
     in inventory-service project, application.yml
     
        spring:
          datasource:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/inventory_services
            username: root
            password: mypassword
          jpa:
            properties:
              hibernate:
                dialect: org.hibernate.dialect.MySQL5InnoDBDialect
            hibernate:
              ddl-auto: update
            generate-ddl: true
            show-sql: true
   
   first start this project, we can change "ddl-auto: create" to create tables, then change back to update
   
   order-services are similiar to inventory-services, no more description
   
   ## Setup and configure Kafka producer/consumer 
   
  1. Install kafka into Mac by download kafka_2.12-3.4.0.tgz from https://kafka.apache.org/downloads
     
     change your .bash_profile , make sure $KAFKA_HOME in path
     
     I got the folder $KAFKA_HOME/shell_script to path of .bash_profile as well
     
     here is my shell script which is not confluent kafka if you test in your local machine no more one broker , partition and replica , one consumer
     
     create_topic.sh
     
        kafka-topic.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic "$1"
        
     list_topic.sh
     
        kafka-topics.sh  --list --bootstrap-server localhost:9092
        
     
     console-producer.sh
        
        kafka-console-producer.sh --broker-list localhost:9092 --topic "$1"
     
     
     console-consumer.sh
     
        kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic "$1" --from-beginning
     
    
    2. Install confluent kafka and zookeeper in your local docker
     
       copy following docker-composer.yml 
     
       
          broker:
            image: confluentinc/cp-kafka:7.3.0
            container_name: broker
            ports:
              # To learn about configuring Kafka for access across networks see
              # https://www.confluent.io/blog/kafka-client-cannot-connect-to-broker-on-aws-on-docker-etc/
              - "9092:9092"
            depends_on:
              - zookeeper
            environment:
              KAFKA_BROKER_ID: 1
              KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
              KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_INTERNAL:PLAINTEXT
              KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092,PLAINTEXT_INTERNAL://broker:29092
              KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
              KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
              KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
     
     
     
     
     
   
