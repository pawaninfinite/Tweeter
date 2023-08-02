# Tweeter

This project consists of two microservices called "tweeterproducer" and "tweeterconsumer."

As per the project requirements, consumers need to subscribe to a specific genre, and any tweet belonging to that genre should be received by the consumer microservices.

To achieve this task, Docker has been utilized to deploy and configure Apache Kafka with Zookeeper. The configuration details are specified in the "docker-compose.yml" file.

To run the "docker-compose.yml" file, you need to navigate to the location where the file is stored and execute the "docker-compose up" command. This will start Docker with Kafka and Zookeeper images.

Keep in mind that Docker should be installed on your system to execute the "docker-compose" command.

Once this setup is completed, Kafka and Zookeeper servers will be up and running. Afterward, we can start the producer and consumer services to post tweets. The consumer service will automatically receive the tweets and store them in the database.

A high-Level flow diagram can be seen in PDF attached.

