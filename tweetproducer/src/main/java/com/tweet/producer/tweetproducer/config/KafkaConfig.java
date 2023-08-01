package com.tweet.producer.tweetproducer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Profile("local")
public class KafkaConfig {

    @Value("${spring.kafka.topic}")
    public String topic;

    @Value("${spring.kafka.producer.bootstrap-servers}")
    public String bootstrapServer;

    @Bean
    public NewTopic tweetEvents(){

        return TopicBuilder
                .name(topic)
                .partitions(1)
                .replicas(1)
                .build();
    }
    private Map<String, Object> kafkaProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", bootstrapServer);
        // Add other Kafka properties as needed
        return props;
    }
    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(kafkaProperties());
    }


}
