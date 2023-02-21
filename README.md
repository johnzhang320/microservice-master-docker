# microservice-master
SpringBoot microservice 

## Environment

  SpringBoot 2.7.8 
  
  JDK 1.8 
  
  Mongodb 4.4.18
  
  mysql 8.0.28
  
  Docker
  
  Kafka
  
## Microservices and applied technologies

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
   
 
      
        
   
   
   
