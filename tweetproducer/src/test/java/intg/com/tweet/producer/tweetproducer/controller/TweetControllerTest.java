package com.tweet.producer.tweetproducer.controller;

import com.tweet.producer.tweetproducer.domain.Tweet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Set;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TweetControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testPostTweetEvent() {
        // Assuming you have your Tweet object ready to be sent as a request body
        Tweet tweet;
        tweet = new Tweet(1,"Hello How Are you","#Welcome");

        ResponseEntity<String> response = restTemplate.postForEntity("/v1/tweet", tweet, String.class);

        // Assertions
        assert response.getStatusCode() == HttpStatus.OK;
        // Add more assertions if needed
    }

    @Test
    public void testFetchAllTweetsByGenre() {
        String genre = "your_genre";

        ResponseEntity<Set> response = restTemplate.getForEntity("/v1/genretweet/{genre}", Set.class, genre);

        // Assertions
        assert response.getStatusCode() == HttpStatus.OK;
        // Add more assertions to validate the response
    }

    @Test
    public void testFetchAllGenreToSubscribe() {
        ResponseEntity<Set> response = restTemplate.getForEntity("/v1/fetchAllGenreToSubscribe", Set.class);

        // Assertions
        assert response.getStatusCode() == HttpStatus.OK;
        // Add more assertions to validate the response
    }

    @Test
    public void testCreateGenreIfNotExist() {
        ResponseEntity<Set> response = restTemplate.postForEntity("/v1/createGenreIfNotExist", null, Set.class);

        // Assertions
        assert response.getStatusCode() == HttpStatus.OK;
        // Add more assertions to validate the response
    }
}
