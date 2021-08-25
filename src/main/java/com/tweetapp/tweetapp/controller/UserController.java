package com.tweetapp.tweetapp.controller;

import com.tweetapp.tweetapp.beans.User;
import com.tweetapp.tweetapp.domain.UserDomain;
import com.tweetapp.tweetapp.dto.request.ForgotPwdRequest;
import com.tweetapp.tweetapp.dto.request.LoginRequest;
import com.tweetapp.tweetapp.dto.request.SignupRequest;
import com.tweetapp.tweetapp.dto.response.UserListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class UserController {

    @Autowired
    private UserDomain userDomain;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userDomain.login(loginRequest);
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(@Valid @RequestBody SignupRequest signupRequest) {
        return userDomain.signup(signupRequest);
    }

    @PostMapping(value = "/forgot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> forgotPwd(@Valid @RequestBody ForgotPwdRequest forgotPwdRequest) {
        return userDomain.changeForgottenPwd(forgotPwdRequest);
    }

    @GetMapping(value = "/users/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllUsers() {
        return userDomain.getAllUsers();
    }

    @GetMapping(value = "/user/search/{searchParam}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserListResponse getUserSearchResults(@PathVariable(name = "searchParam") String searchParam){
        return userDomain.getUserSearchResults(searchParam);
    }

}
