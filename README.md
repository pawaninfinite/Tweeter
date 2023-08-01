# Tweeter

This project has 2 microservices created one is tweeterproducer and another one is tweeterconsumer.

As per the requirement, consumer are required to subscibe to particular genre and whatever tweet belongs to that genre should be received to consumer microservices.

To accomplish the task, docker has been used to deploy and configure Apache kafka with zookeeper. The details configuration is been mentioned in docker-compose.yml.

To run the docker-compose.yml file you need to open the location where file is been kept and then you have to run the docker-compose up command to run the docker with kafka and zookeper images.

Please note docker should be installed on your system to run the docker-compose command.


This will setup our kafka and zookeper server.

and the we can start the producer and consumer services to tweet the message and consumer will automatically recieved the message ans persist the same in database.



