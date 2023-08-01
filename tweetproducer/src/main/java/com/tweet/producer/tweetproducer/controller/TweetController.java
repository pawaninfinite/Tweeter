package com.tweet.producer.tweetproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tweet.producer.tweetproducer.domain.Hashtag;
import com.tweet.producer.tweetproducer.domain.Tweet;
import com.tweet.producer.tweetproducer.service.TweetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@Slf4j
public class TweetController {
    @Autowired
    private TweetService tweetService;

    @PostMapping("/v1/tweet")
    public ResponseEntity<String> postTweetEvent(
            @RequestBody Tweet tweets
    ) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {

        log.info("This is the tweet controller!!");
        tweetService.sendTweetEvent(tweets);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Ok");
    }

    @GetMapping("/v1/genretweet/{genre}")
    public ResponseEntity<Set<String>> fetchAllTweetsByGenre(@PathVariable String genre) throws ExecutionException, InterruptedException, IOException {

        Set<String> tweetList=tweetService.fetchAllTweetByGenre(genre);
        return ResponseEntity.status(HttpStatus.OK)
                .body(tweetList);

    }

    /**
     * This method will return all the existing genre for client to subscibe.
     * @return ResponseEntity<Set<String>>
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */
    @GetMapping("/v1/fetchAllGenreToSubscribe")
    public ResponseEntity<Set<String>> fetchAllGenreToSubscribe() throws ExecutionException, InterruptedException, IOException {

        Set<String> genreList=tweetService.fetchAllGenreToSubscribe();
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreList);

    }

    @PostMapping("/v1/createGenreIfNotExist")
    public ResponseEntity<Set<String>> createGenreIfNotExist() throws IOException {
      Set<String>  genreList =tweetService.createGenreToSubscribe();
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreList);
    }


}
