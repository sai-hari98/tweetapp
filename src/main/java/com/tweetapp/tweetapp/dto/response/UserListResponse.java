package com.tweetapp.tweetapp.dto.response;

import com.tweetapp.tweetapp.beans.User;
import com.tweetapp.tweetapp.dto.BaseDto;
import lombok.Data;

import java.util.List;

@Data
public class UserListResponse extends BaseDto {

    private List<User> users;
}
