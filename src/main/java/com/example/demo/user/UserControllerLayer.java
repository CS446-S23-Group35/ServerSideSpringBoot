package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="rest")
public class UserControllerLayer {

    @Autowired
    UserServiceLayer userServiceLayer;

    //return the User data for the user with the associated username as per the incoming request
    @GetMapping
    public @ResponseBody User getUsers(@RequestParam("username") String userName){

        return userServiceLayer.getUsers(userName);
    }




}
