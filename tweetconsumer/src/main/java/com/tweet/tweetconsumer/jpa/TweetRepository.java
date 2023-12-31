package com.tweet.tweetconsumer.jpa;

import com.tweet.tweetconsumer.entity.Tweet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends CrudRepository<Tweet, Integer> {
}
