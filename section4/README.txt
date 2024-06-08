This section covers the example of 
dockerize the account app using the Dockerfile//this file is parallel to target folder//docker build . -t elearn/accounts-ms:s4
dockerize the cards app using googlejib //mvn compile jib:dockerBuild
dockerize the loans app using buildpacks//mvn spring-boot:build-image

docker-compose.yaml//created parallel to microservices and is used create a conatiner from the already build images
 we can refer the Dockerfile inside a docker-compose file to build the images as well but for image creation we only need to use the Dockerfile

