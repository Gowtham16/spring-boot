package com.practice.spring.boot.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.practice.spring.boot.constants.ResponseMessageconstants;
import com.practice.spring.boot.entity.User;
import com.practice.spring.boot.exception.ExceptionHandler;
import com.practice.spring.boot.service.LoginService;
import com.practice.spring.boot.service.UserService;
import com.practice.spring.boot.utils.ResponseObject;


@RestController
@RequestMapping(value = "/api/account")
public class AccountApi {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UserService userService; 
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseObject onUserLogin(@RequestBody User user) {
        ResponseObject responseObject = new ResponseObject();
        try {
        	responseObject.setUser(loginService.onUserLogin(user));
        	responseObject.setMessage(ResponseMessageconstants.LOGIN_SUCCESS);
        } catch (Exception e){
        	ExceptionHandler.setMessageCode(e, responseObject);
        }
        return responseObject;
    }
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseObject getCurrentUser() {
        ResponseObject responseObject = new ResponseObject();
        try {
        	responseObject.setUser(loginService.getCurrentUser());
        } catch (Exception e){
        	ExceptionHandler.setMessageCode(e, responseObject);
        }
        return responseObject;
    }
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseObject signup(@RequestBody User user) {
        ResponseObject responseObject = new ResponseObject();
        try {
        	responseObject.setUser(userService.addUser(user));
        	responseObject.setMessage(ResponseMessageconstants.USER_ADDED_SUCCESSFULLY);
        } catch (Exception e){
        	ExceptionHandler.setMessageCode(e, responseObject);
        }
        return responseObject;
    }
}
