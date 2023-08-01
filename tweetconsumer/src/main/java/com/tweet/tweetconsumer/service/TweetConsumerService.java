package com.tweet.tweetconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweet.tweetconsumer.entity.Tweet;
import com.tweet.tweetconsumer.jpa.TweetRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TweetConsumerService {

    private AdminClient adminClient;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Autowired
    private KafkaListenerEndpointRegistry endpointRegistry;

    @Autowired
    private TweetRepository  tweetRepository;

    @Autowired
    ObjectMapper objectMapper;

    public void processTweetEvent(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {

        Tweet tweet=objectMapper.readValue(consumerRecord.value(),Tweet.class);
        tweetRepository.save(tweet);

    }

    @Autowired
    public TweetConsumerService(ObjectMapper objectMapper, KafkaAdmin kafkaAdmin) {

        this.adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());

    }

    public Set<String> fetchAllGenreToSubscribe() throws ExecutionException, InterruptedException {

        ListTopicsResult topicsResult = adminClient.listTopics();

        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(false); // Include internal topics if needed

        Set<String> topics = adminClient.listTopics(options).names().get();

        return topics;
    }





    public Set<String> fetchAllSubscribedGenre() {

        Set<String> subscribedTopics = new HashSet<>();

        Collection<MessageListenerContainer> containers = endpointRegistry.getListenerContainers();
        for (MessageListenerContainer container : containers) {
            Set<String> topics = container.getAssignedPartitions().stream()
                    .map(tp -> tp.topic())
                    .collect(Collectors.toSet());
            subscribedTopics.addAll(topics);
        }

        return subscribedTopics;
    }

}
