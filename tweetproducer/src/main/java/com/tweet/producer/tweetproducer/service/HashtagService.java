package com.tweet.producer.tweetproducer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweet.producer.tweetproducer.domain.Hashtag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HashtagService {


    private final ObjectMapper objectMapper;
    public HashtagService(){
        this.objectMapper=new ObjectMapper();
    }

    public List<Hashtag> readHashtagsFromJsonFile() throws IOException {
        File file = new File("src/main/resources/hashtags.json"); // Path to your JSON file
        return Arrays.asList(objectMapper.readValue(file, Hashtag[].class));
    }

    public void addNewHashtagIfNotExists(Hashtag newHashtag) throws IOException {
        List<Hashtag> hashtags = readHashtagsFromJsonFile();

        if (!hashtagExists(hashtags, newHashtag.hashtag())) {
            addNewHashtag(hashtags, newHashtag);
            writeHashtagsToFile(hashtags);
        }
    }

    private boolean hashtagExists(List<Hashtag> hashtags, String newHashtag) {
        return hashtags.stream().anyMatch(hashtag -> hashtag.hashtag().equalsIgnoreCase(newHashtag));
    }

    private void writeHashtagsToFile(List<Hashtag> hashtags) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/main/resources/hashtags.json"); // Path to your JSON file
        objectMapper.writeValue(file, hashtags);
    }

    private void addNewHashtag(List<Hashtag> hashtags, Hashtag newHashtag) {
        hashtags.add(newHashtag);
    }

    public List<Hashtag> filterHashtagsByGenre(List<Hashtag> hashtags, String genreToFilter) {
        return hashtags.stream()
                .filter(hashtag -> hashtag.genre().contains(genreToFilter))
                .collect(Collectors.toList());
    }
}
