package com.tweetapp.tweetapp.domain;

import com.tweetapp.tweetapp.beans.Reply;
import com.tweetapp.tweetapp.beans.Tweet;
import com.tweetapp.tweetapp.constants.TweetAppConstants;
import com.tweetapp.tweetapp.dto.request.TweetRequest;
import com.tweetapp.tweetapp.dto.response.ReplyDto;
import com.tweetapp.tweetapp.dto.response.TweetDto;
import com.tweetapp.tweetapp.dto.response.TweetsResponse;
import com.tweetapp.tweetapp.repository.MongoRepository;
import com.tweetapp.tweetapp.utils.TweetAppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TweetDomain {

    /*private static final DateTimeFormatter TWEET_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssSSSZ");*/

    @Autowired
    private MongoRepository mongoRepository;

    public TweetsResponse getAllTweets(){
        TweetsResponse response = new TweetsResponse();
        List<Tweet> tweets = mongoRepository.getAllTweets();
        tweets = CollectionUtils.isEmpty(tweets) ? new ArrayList<>()
                : tweets;
        List<TweetDto> tweetsForResponse = processTweets(tweets);
        response.setTweets(tweetsForResponse);
        return response;
    }

    public TweetsResponse getUserTweets(String userId){
        TweetsResponse response = new TweetsResponse();
        List<Tweet> tweets = mongoRepository.getUserTweets(userId);
        tweets = CollectionUtils.isEmpty(tweets) ? new ArrayList<>()
                : tweets;
        List<TweetDto> tweetsForResponse = processTweets(tweets);
        response.setTweets(tweetsForResponse);
        return response;
    }

    public ResponseEntity<Object> postTweet(TweetRequest tweetRequest){
        tweetRequest.getTweet().setTimeStamp(new Date());
        tweetRequest.getTweet().setId(generateTweetId());
        String content = tweetRequest.getTweet().getContent();
        tweetRequest.getTweet().setHashTags(findHashTags(content));
        mongoRepository.insertTweet(tweetRequest.getTweet());
        return TweetAppUtils.getSuccessResponse(TweetAppConstants.TWEET_INSERT_SUCCESS, tweetRequest.getTweet().getId());
    }

    public ResponseEntity<Object> updateTweet(String userName, TweetRequest tweetRequest, String tweetId){
        boolean isSuccess = mongoRepository.updateTweetContent(userName, tweetRequest.getTweet().getContent(), tweetId);
        if(isSuccess){
            return TweetAppUtils.getSuccessResponse(TweetAppConstants.TWEET_UPDATE_SUCCESS, null);
        }else{
            return TweetAppUtils.getErrorResponse(TweetAppConstants.UPDATE_TWEET_FAILED, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> deleteTweet(String username, String id){
        boolean isSuccess = mongoRepository.deleteTweet(username, id);
        if(isSuccess){
            return TweetAppUtils.getSuccessResponse(TweetAppConstants.TWEET_DELETE_SUCCESS, null);
        }else{
            return TweetAppUtils.getErrorResponse(TweetAppConstants.TWEET_DELETE_FAIL, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> likeTweet(String userName, String id){
        Tweet tweet = mongoRepository.getTweet(id);
        if(tweet != null){
            List<String> likedBy = CollectionUtils.isEmpty(tweet.getLikedBy()) ? new ArrayList<>() : tweet.getLikedBy();
            if(likedBy.contains(userName)){
                likedBy.remove(userName);
            }else{
                likedBy.add(userName);
            }
            tweet.setLikedBy(likedBy);
            mongoRepository.updateTweet(tweet);
            return TweetAppUtils.getSuccessResponse(TweetAppConstants.LIKE_REQ_SUCCESS, null);
        }else{
            return TweetAppUtils.getErrorResponse(TweetAppConstants.INVALID_TWEET_ID, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> replyTweet(String id, Reply reply){
        Tweet tweet = mongoRepository.getTweet(id);
        if(tweet != null){
            List<Reply> replies = CollectionUtils.isEmpty(tweet.getReplies()) ? new ArrayList<>() : tweet.getReplies();
            reply.setTimeStamp(new Date());
            reply.setHashTags(findHashTags(reply.getContent()));
            replies.add(reply);
            tweet.setReplies(replies);
            mongoRepository.updateTweet(tweet);
            return TweetAppUtils.getSuccessResponse(TweetAppConstants.REPLY_SUCCESS_MSG, null);
        }else{
            return TweetAppUtils.getErrorResponse(TweetAppConstants.INVALID_TWEET_ID, HttpStatus.BAD_REQUEST);
        }
    }

    private String generateTweetId(){
        return UUID.randomUUID().toString().replace("-","").substring(0, 10);
    }

/*    private String getCurrentUTCTimeStamp(){
        ZoneId utcId = ZoneId.of("UTC");
        ZonedDateTime utcTime = ZonedDateTime.ofInstant(Instant.now(), utcId);
        return utcTime.format(TWEET_TIMESTAMP_FORMAT);
    }*/

    private List<String> findHashTags(String content){
        String copiedStr = new String(content);
        copiedStr.replace("\n"," ");
        copiedStr.replace("\r", " ");
        List<String> words = Arrays.asList(content.split(" "));
        return words.stream().filter(word -> word.startsWith("#")).collect(Collectors.toList());
    }

    private List<TweetDto> processTweets(List<Tweet> tweets){
        List<TweetDto> tweetDtoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(tweets)){
            Comparator timeStampComparator = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    try {
                        Field field1 = o1.getClass().getField("timestamp");
                        field1.setAccessible(true);
                        Field field2 = o1.getClass().getField("timestamp");
                        field2.setAccessible(true);
                        Date timestamp1 = (Date) field1.get(o1);
                        Date timestamp2 = (Date) field2.get(o2);
                        if(timestamp1.after(timestamp2)){
                            return 1;
                        }else if(timestamp1.before(timestamp2)){
                            return -1;
                        }else{
                            return 0;
                        }
                    } catch (NoSuchFieldException | IllegalAccessException exception) {
                        log.error("Error while sorting the tweets by timestamp: {}",exception.getMessage());
                    }
                    return 0;
                }
            };
            tweets.sort(timeStampComparator);
            tweetDtoList = tweets.stream().map(tweet -> {
                TweetDto tweetDto = new TweetDto();
                BeanUtils.copyProperties(tweet, tweetDto);
                tweetDto.setTimeAgo(getContextualTimeMsg(tweet.getTimeStamp()));
                List<Reply> replies = CollectionUtils.isEmpty(tweet.getReplies()) ? new ArrayList<>() : tweet.getReplies();
                Collections.sort(replies, timeStampComparator);
                List<ReplyDto> replyDtoList = replies.stream().map(reply -> {
                    ReplyDto replyDto = new ReplyDto();
                    BeanUtils.copyProperties(reply, replyDto);
                    replyDto.setTimeAgo(getContextualTimeMsg(reply.getTimeStamp()));
                    return replyDto;
                }).collect(Collectors.toList());
                tweetDto.setReplies(replyDtoList);
                return tweetDto;
            }).collect(Collectors.toList());
        }
        return tweetDtoList;
    }

    private String getContextualTimeMsg(Date date){
        String contextualTimeMsg = null;
        long timeDiff = new Date().getTime() - date.getTime();
        long secDiff = (timeDiff/1000)%60;
        long minDiff = (timeDiff/(1000*60))%60;
        long hrDiff = (timeDiff/(1000*60*60))% 24;
        long daysDiff = (timeDiff/(1000*60*60*24))%365;
        if(daysDiff > 0){
            contextualTimeMsg = daysDiff + "d ago";
        }else if(hrDiff > 0){
            contextualTimeMsg = hrDiff + "h ago";
        }else if(minDiff > 0){
            contextualTimeMsg = minDiff + "m ago";
        }else if(secDiff > 0){
            contextualTimeMsg = secDiff + "s ago";
        }
        return contextualTimeMsg;
    }


}
