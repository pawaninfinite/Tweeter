package com.tweet.producer.tweetproducer.domain;

import java.util.List;

public record Hashtag(Integer id, String hashtag, List<String> genre) {
}
