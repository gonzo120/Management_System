package com.inn.cafe.cominncafe.restlmpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.cominncafe.constents.RestaurantConstants;
import com.inn.cafe.cominncafe.rest.UserRest;
import com.inn.cafe.cominncafe.service.UserService;
import com.inn.cafe.cominncafe.utils.RestaurantUtils;

import java.util.Map;

@RestController
public class UserRestImpl implements UserRest{

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
