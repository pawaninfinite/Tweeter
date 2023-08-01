package com.tweet.tweetconsumer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Tweet {

    @Id
    @GeneratedValue
    private Integer tweetId;

    String tweet;

    String hashtags;



}
