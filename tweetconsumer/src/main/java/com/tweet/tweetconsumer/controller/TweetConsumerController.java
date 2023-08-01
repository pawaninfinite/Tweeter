package com.tweet.tweetconsumer.controller;

import com.tweet.tweetconsumer.service.TweetConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
public class TweetConsumerController {

    @Autowired
    private TweetConsumerService consumerService;



    @GetMapping("/v1/fetchAllGenreToSubscribe")
    public ResponseEntity<Set<String>> fetchAllGenreToSubscribe() throws ExecutionException, InterruptedException, IOException {

        Set<String> genreList=consumerService.fetchAllGenreToSubscribe();
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreList);

    }


    @GetMapping("/v2/fetchAllSubscribedGenre")
    public ResponseEntity<Set<String>>  fetchAllSubscribedGenre(){
        Set<String> subscribedGenre=consumerService.fetchAllSubscribedGenre();
        return ResponseEntity.status(HttpStatus.OK)
                .body(subscribedGenre);
    }



}
