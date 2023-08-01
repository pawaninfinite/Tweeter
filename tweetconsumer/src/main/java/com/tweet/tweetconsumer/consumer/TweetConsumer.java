package com.tweet.tweetconsumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tweet.tweetconsumer.service.TweetConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TweetConsumer {
    private final KafkaConsumer<String, String> kafkaConsumer;

    @Autowired
    private TweetConsumerService tweetService;

    @Autowired
    public TweetConsumer(KafkaConsumer<String, String> kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }


    /**
     * This method will listen to topic mention in application.yml fille and
     * save the data in DB using sprin jpa repository.
     * @param consumerRecord
     * @throws JsonProcessingException
     */
    @KafkaListener(topics = "${spring.kafka.topic}")
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {

        try {
            // Your existing processing logic
            tweetService.processTweetEvent(consumerRecord);
        } catch (JsonProcessingException ex) {
            // Log the exception details for debugging
            ex.printStackTrace();
            // You can also log the contents of the problematic message:
            log.error("Received Message: {} " , consumerRecord.value());
        }

        log.info("Consumer record : {}", consumerRecord);


    }
}
