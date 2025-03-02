package com.deepak.proexpenditure.pro_expenditure.controller;

import com.deepak.proexpenditure.pro_expenditure.entity.Users;
import com.deepak.proexpenditure.pro_expenditure.service.UserService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class userController {
    @Autowired
    UserService2 users;

    @GetMapping("/users")
    public List<Users> sayHello() {
        return users.getUsers();
    }
    @GetMapping("/users/{userID}")
    public Users getUserByID(@PathVariable int userID){
        return users.getUserByIds(userID);
    }
    @PostMapping("/users")
    public List<Users> addUser(@RequestBody Users user){
        users.adduser(user);
        return sayHello();
    }
}
