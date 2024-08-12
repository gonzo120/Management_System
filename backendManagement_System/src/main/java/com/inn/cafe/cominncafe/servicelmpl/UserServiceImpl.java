package com.inn.cafe.cominncafe.servicelmpl;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.cominncafe.POJO.User;
import com.inn.cafe.cominncafe.constents.RestaurantConstants;
import com.inn.cafe.cominncafe.dao.UserDao;
import com.inn.cafe.cominncafe.service.UserService;
import com.inn.cafe.cominncafe.utils.RestaurantUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if(validateSignUpMap(requestMap)) {
           
                User user = userDao.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userDao.save(getUserFromMap(requestMap));
                    return RestaurantUtils.getResponseEntity("Succesfully Registered", HttpStatus.OK);
                }else{
                    return RestaurantUtils.getResponseEntity("Email already exits", HttpStatus.BAD_REQUEST);
                }
                
            } else {
                
                return RestaurantUtils.getResponseEntity(RestaurantConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        
    }

    private boolean validateSignUpMap(Map<String, String>requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber") 
        && requestMap.containsKey("email") && requestMap.containsKey("password")
        ){
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("ContactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

}
