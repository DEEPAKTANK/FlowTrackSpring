package com.deepak.proexpenditure.pro_expenditure.controller;

import com.deepak.proexpenditure.pro_expenditure.repository.Users;
import com.deepak.proexpenditure.pro_expenditure.service.User1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class User1controller {
    @Autowired
    User1Service users;

    @PostMapping("/users1")
    public void addUser(@RequestBody Users user){
        users.adduser(user);
    }

}
