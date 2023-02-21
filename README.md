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
  
  Resilince4J ---- circuit breaker and fallback support, new replace of hystrix
  
  KAFKA event driven ---- kafka producer and consumer Serialize/Deserialize Json Object via using confluent zookeeper and kafka docker-compose
  
  Product-Service ---- Eureka Client, create products in Mongodb,  authenticate mongo, configure MongoRepository, provide 5 rest APIs
  
  Inventory-Service ---- Eureka/Feign Client, create inventory in MySQL, productId, product name, price call product service by product 
  
  Order-Service ---- Eureka/Feign Client, Resilince4J, Kafka Producer, place order to MySQL, check inventory quantity, send event to notification
  
  Notification ---- Kafka Consumer, listening Order-Service Order Event 
  
  
  
  
