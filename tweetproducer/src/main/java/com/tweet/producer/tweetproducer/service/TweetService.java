package com.tweet.producer.tweetproducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweet.producer.tweetproducer.domain.Hashtag;
import com.tweet.producer.tweetproducer.domain.Tweet;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TweetService {

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final AdminClient adminClient;



    @Autowired
    private  HashtagService hashtagService;

    @Value("${spring.kafka.topic}")
    public String topic;

    @Autowired
    public TweetService(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper, KafkaAdmin kafkaAdmin) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());

    }

    public CompletableFuture<SendResult<Integer, String>> sendTweetEvent(Tweet tweet) throws JsonProcessingException {

        var key = tweet.id();
        var value = objectMapper.writeValueAsString(tweet);

        // 1. blocking call - get metadata about the kafka cluster
        //2.Send message happens - Returns a CompletableFuture
        var completableFuture = kafkaTemplate.send(topic, key, value);

        return completableFuture
                .whenComplete((sendResult, throwable) -> {
                    if (throwable != null) {
                        handleFailure(key, value, throwable);
                    } else {
                        handleSuccess(key, value, sendResult);
                    }
                });

    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        log.info("Message Sent Successfully for the key : {} and the value : {} , partition is {} ",
                key, value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error sending the message and the exception is {} ", ex.getMessage(), ex);
    }

    public Set<String> fetchAllTweetByGenre(String genre)
            throws ExecutionException, InterruptedException, IOException {

        List<Hashtag> hashtaglist= hashtagService.readHashtagsFromJsonFile();

            List<Hashtag> filterHashtag=hashtagService.filterHashtagsByGenre(hashtaglist,genre);

            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(true); // Set to true if you want to include internal topics

            ListTopicsResult topicsResult = adminClient.listTopics(options);
            Map<String, TopicListing> topics = topicsResult.namesToListings().get();

        List<String> filterhashtaglist=
                filterHashtag.stream()
                        .map(hashtag -> hashtag.hashtag()).collect(Collectors.toList());



        Map<String, TopicListing> topicmatchingwithgenre=topics.entrySet().stream()
                .filter(entry -> filterhashtaglist.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            return topicmatchingwithgenre.keySet();

    }


    //This method will fetch all the Genre related to particular hashtag


    public Set<String> fetchAllGenreToSubscribe() throws ExecutionException, InterruptedException {
        ListTopicsResult topicsResult = adminClient.listTopics();


        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(false); // Include internal topics if needed

        Set<String> topics = adminClient.listTopics(options).names().get();

        return topics;
    }

    public Set<String> createGenreToSubscribe() throws IOException {

        List<Hashtag> hashtaglist=hashtagService.readHashtagsFromJsonFile();

        Set<String> genreSet=hashtaglist.stream()
                .flatMap(hashtag -> hashtag.genre().stream())
                .collect(Collectors.toSet());

        genreSet.stream()
                .forEach(genre->createTopic(genre,1,(short)1) );

        return genreSet;

    }

    public void createTopic(String topicName, int numPartitions, short replicationFactor) {
        NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
        try {
            adminClient.createTopics(Collections.singleton(newTopic)).all().get();
            System.out.println("Topic created successfully: " + topicName);
        } catch (Exception e) {
            if (e.getCause() instanceof TopicExistsException) {
                log.error("Topic already exists: {}" , topicName);
            } else {
                log.error("Error creating topic: {topicName} Error stack : {e.printStackTrace()}");
            }
    }
}
}