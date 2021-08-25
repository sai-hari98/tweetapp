package com.tweetapp.tweetapp.domain;

import com.tweetapp.tweetapp.beans.User;
import com.tweetapp.tweetapp.config.TweetAppConfig;
import com.tweetapp.tweetapp.constants.TweetAppConstants;
import com.tweetapp.tweetapp.dto.BaseDto;
import com.tweetapp.tweetapp.dto.request.ForgotPwdRequest;
import com.tweetapp.tweetapp.dto.request.LoginRequest;
import com.tweetapp.tweetapp.dto.request.SignupRequest;
import com.tweetapp.tweetapp.dto.response.ErrorResponse;
import com.tweetapp.tweetapp.dto.response.LoginResponse;
import com.tweetapp.tweetapp.dto.response.SuccessResponse;
import com.tweetapp.tweetapp.dto.response.UserListResponse;
import com.tweetapp.tweetapp.producer.ForgotPwdProducer;
import com.tweetapp.tweetapp.repository.MongoRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class UserDomain {

    @Autowired
    private TweetAppConfig config;

    @Autowired
    private MongoRepository mongoRepository;

    @Autowired
    private ForgotPwdProducer forgotPwdProducer;

    public ResponseEntity<Object> login(LoginRequest request){
        User user = mongoRepository.findUserByUserName(request.getUserName());
        String errorMsg = null;
        String errorCode = null;
        if(user == null){
            errorMsg = TweetAppConstants.INVALID_UNAME;
            errorCode = TweetAppConstants.INVALID_UNAME_CD;
        }
        String pwdEncoded = Base64.getEncoder().encodeToString(request.getPassword().getBytes());
        if(user != null && !user.getPwd().equals(pwdEncoded)){
            errorMsg = TweetAppConstants.INVALID_PWD;
            errorCode = TweetAppConstants.INVALID_PWD_CD;
        }
        if(errorMsg != null){
            ErrorResponse response = new ErrorResponse();
            response.setMessage(errorMsg);
            response.setCode(errorCode);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }
        LoginResponse response = new LoginResponse();
        response.setJwt(getJwtToken(user.getUserName()));
        response.setUserName(user.getUserName());
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> signup(SignupRequest signupRequest){
        User user = mongoRepository.findUserByUserName(signupRequest.getUserName());
        if(user != null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage(TweetAppConstants.USER_EXIST);
            errorResponse.setCode(TweetAppConstants.SIGNUP_FAILED_CD);
            return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        User newUser = new User();
        BeanUtils.copyProperties(signupRequest, newUser);
        newUser.setPwd(Base64.getEncoder().encodeToString(signupRequest.getPwd().getBytes()));
        newUser.setId(newUser.getUserName());
        mongoRepository.createUser(newUser);
        SuccessResponse successResp = new SuccessResponse();
        successResp.setMessage(TweetAppConstants.SIGNUP_SUCCESS);
        return new ResponseEntity<Object>(successResp, HttpStatus.OK);
    }

    public ResponseEntity<Object> changeForgottenPwd(ForgotPwdRequest forgotPwdRequest){
        String reqId = UUID.randomUUID().toString();
        forgotPwdProducer.publishForgotPwdReq(reqId, forgotPwdRequest);
        /*User user = mongoRepository.findByUsernamePhNo(forgotPwdRequest.getUserName(), forgotPwdRequest.getPhno());
        if(user == null){
            ErrorResponse errResp = new ErrorResponse();
            errResp.setMessage(TweetAppConstants.USER_NO_EXIST);
            errResp.setCode(TweetAppConstants.USER_NO_EXIST_CD);
            return new ResponseEntity<Object>(errResp, HttpStatus.BAD_REQUEST);
        }
        String encPwd = Base64.getEncoder().encodeToString(forgotPwdRequest.getNewPwd().getBytes());
        if(user != null && user.getPwd().equals(encPwd)){
            ErrorResponse errResp = new ErrorResponse();
            errResp.setMessage(TweetAppConstants.USER_PWD_SAME);
            errResp.setCode(TweetAppConstants.USER_PWD_SAME_CD);
            return new ResponseEntity<Object>(errResp, HttpStatus.BAD_REQUEST);
        }
        user.setPwd(encPwd);
        mongoRepository.updateUserPwd(user);*/
        SuccessResponse response = new SuccessResponse();
        response.setMessage(TweetAppConstants.PWD_UPDATE_SUCCESS);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> getAllUsers(){
        List<User> users = mongoRepository.getAllUsers();
        users.stream().forEach(user -> user.setPwd(null));
        UserListResponse response = new UserListResponse();
        response.setUsers(users);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    public UserListResponse getUserSearchResults(String searchParam){
        List<User> users = mongoRepository.findUsersBySearchText(searchParam);
        users.stream().forEach(user -> user.setPwd(null));
        UserListResponse response = new UserListResponse();
        response.setUsers(users);
        return response;
    }

    /**
     * To generate JWT for login
     * @param userName of the user to be logged in
     * @return JWT token to be used for consecutive requests
     */
    private String getJwtToken(String userName){
        JwtBuilder builder = Jwts.builder();
        builder.setSubject(userName);
        // Set the token issue time as current time
        builder.setIssuedAt(new Date());
        // Set the token expiry as 20 minutes from now
        builder.setExpiration(new Date((new Date()).getTime() + 1200000));
        builder.signWith(SignatureAlgorithm.HS256, config.getSigningKey());
        String token = builder.compact();
        return token;
    }
}
