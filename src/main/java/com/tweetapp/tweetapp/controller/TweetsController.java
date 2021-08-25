package com.tweetapp.tweetapp.controller;

import com.tweetapp.tweetapp.beans.Reply;
import com.tweetapp.tweetapp.beans.Tweet;
import com.tweetapp.tweetapp.domain.TweetDomain;
import com.tweetapp.tweetapp.dto.request.TweetRequest;
import com.tweetapp.tweetapp.dto.response.TweetsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin(origins = "http://localhost:3000")
public class TweetsController {

    @Autowired
    private TweetDomain tweetDomain;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public TweetsResponse getAllTweets(){
        return tweetDomain.getAllTweets();
    }

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TweetsResponse getUserTweets(@PathVariable(name = "username")String username){
        return tweetDomain.getUserTweets(username);
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postTweet(@Valid @RequestBody TweetRequest tweet){
        return tweetDomain.postTweet(tweet);
    }

    @PutMapping(value = "/{username}/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateTweet(@Valid @RequestBody TweetRequest tweetRequest,
                                              @PathVariable(name = "username")String username,
                                              @PathVariable(name = "id")String tweetId){
        return tweetDomain.updateTweet(username, tweetRequest, tweetId);
    }

    @DeleteMapping(value = "/{username}/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteTweet(@PathVariable(name = "username")String username,
                                              @PathVariable(name = "id")String tweetId){
        return tweetDomain.deleteTweet(username, tweetId);
    }

    @PutMapping(value = "/{username}/like/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> likeTweet(@PathVariable(name = "username")String username,
                                            @PathVariable(name = "id")String tweetId){
        return tweetDomain.likeTweet(username, tweetId);
    }

    @PostMapping(value = "/reply/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> replyTweet(@Valid @RequestBody Reply reply,
                                             @PathVariable(name = "id")String tweetId){
        return tweetDomain.replyTweet(tweetId, reply);
    }
}
