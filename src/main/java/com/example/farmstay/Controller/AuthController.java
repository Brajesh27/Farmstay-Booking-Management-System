package com.example.farmstay.Controller;

import com.example.farmstay.Model.User;
import com.example.farmstay.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/register")
    public String ShowRegister(){
        return "register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String phone,
                           @RequestParam String username,
                           @RequestParam String password,
                           Model model){
        String error = userService.checkPhone(phone);
        if(error == null){
            error = userService.checkPasswordRules(password);
        }
        if(error != null){
            model.addAttribute("error", error);
            return "register";
        }
        boolean ok = userService.register(username, password, name, phone);
        if(!ok){
            model.addAttribute("error","That username is already taken. Try another one.");
            return "register";
        }
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String ShowLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model){
        User user = userService.login(username, password);
        if(user==null){
            model.addAttribute("error","Wrong username or password.");
            return "login";
        }
        session.setAttribute("user",user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
