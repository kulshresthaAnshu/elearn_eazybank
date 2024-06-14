This section covers the example of 
1. Replace H2 with MySQL 
2. Use MySQL docker container
3. update docker compose

In this example we have removed the changes for spring cloud bus and web hook .The below depedency to keep the project lightweight are removed. This will remove the need of rabbitMQ conatiner
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-config-monitor</artifactId>
</dependency>

4. To execute the schema.sql file we need to make sure that the table name is as same as entity