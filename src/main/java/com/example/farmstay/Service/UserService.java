package com.example.farmstay.Service;

import com.example.farmstay.Model.User;
import com.example.farmstay.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
        // create the admin account only the first time
        if(!repository.existsById("a")){
            repository.save(new User("a","123","Brajesh","123456789",true));
        }
    }

    public User findByUsername(String username){
        return repository.findById(username).orElse(null);
    }

    public boolean register(String username, String password, String name, String phone){
        if(findByUsername(username) != null){
            return false;
        }
        repository.save(new User(username,password,name,phone,false));
        return true;
    }

    public User login(String username, String password){
        User u = findByUsername(username);
        if(u != null && u.CheckPassword(password)){
            return u;
        }
        return null;
    }
    // returns an error message, or null if the password is OK
    public String checkPasswordRules(String password){
        if(password.length() < 8){
            return "Password must be at least 8 characters long.";
        }
        if(!password.matches(".*[A-Za-z].*")){
            return "Password must contain at least one letter.";
        }
        if(!password.matches(".*[0-9].*")){
            return "Password must contain at least one number.";
        }
        if(!password.matches(".*[^A-Za-z0-9].*")){
            return "Password must contain at least one special character (like @ # $ !).";
        }
        return null;
    }

    // returns an error message, or null if the phone number is OK
    public String checkPhone(String phone){
        if(!phone.matches("[0-9]{10}")){
            return "Phone number must be exactly 10 digits (numbers only).";
        }
        return null;
    }

    // call this after changing a user (profile, password)
    public void save(User user){
        repository.save(user);
    }
}