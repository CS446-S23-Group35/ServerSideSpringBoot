package com.example.demo.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceLayer {

    @Autowired
    UserDBLayer userDBLayer;
    public User getUsers(String userName){

        return userDBLayer.getUsers(userName);
    }
}
