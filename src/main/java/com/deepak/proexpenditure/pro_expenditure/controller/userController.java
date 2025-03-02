package com.deepak.proexpenditure.pro_expenditure.controller;

import com.deepak.proexpenditure.pro_expenditure.repository.Users;
import com.deepak.proexpenditure.pro_expenditure.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class userController {
    @Autowired
    UserService users;

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
