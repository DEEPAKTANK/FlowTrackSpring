package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.repository.Users;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class UserService {
    int userid=100;
    List <Users> users = new ArrayList<>(Arrays.asList(new Users(100,"Deeepak","Deepak123"),
            new Users(101,"Kumar","Kumar123"),
            new Users(103,"Tank","Tank123")));
    public List<Users> getUsers(){
        return users;
    }
    public int getUserid(){
        this.userid++;
        return userid;
    }
    public Users getUserByIds(int userID){
        return users.stream().filter(p-> p.getUserID()==userID).findFirst().orElse(new Users(100,"Deeepak","Deepak123"));
    }
    public Users getUserByNames(String userName){
        return users.stream().filter(p-> p.getUserName().equalsIgnoreCase(userName)).findFirst().orElse(new Users(100,"Deeepak","Deepak123"));
    }
    public void adduser(Users user){
        users.add(user);
    }
}
