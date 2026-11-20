package com.example.farmstay.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class User {
    @Id
    private String username;
    private String password;
    private String name;
    private String phone;
    private boolean admin;
    public User(String username, String password, String name, String phone, boolean admin){
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return admin;
    }
    public boolean CheckPassword(String EnteredPassword){
        return password.equals(EnteredPassword);
    }

    public void updateDetails(String name, String phone){
        this.name = name;
        this.phone = phone;
    }
    public boolean changePassword(String oldPassword, String newPassword){
        if(!CheckPassword(oldPassword)){
            return false;
        }
        this.password = newPassword;
        return  true;
    }

}
