package com.example.farmstay.Controller;

import com.example.farmstay.Model.User;
import com.example.farmstay.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {
    private UserService userService;

    public ProfileController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String ShowProfile(HttpSession session){
        if(session.getAttribute("user") == null){
            return "redirect:/login";
        }
        return "profile";
    }

    @PostMapping("/profile")
    public String UpdateProfile(@RequestParam String name,
                                @RequestParam String phone,
                                HttpSession session,
                                Model model){
        User user = (User) session.getAttribute("user");
        if(user == null){
            return "redirect:/login";
        }
        String error = userService.checkPhone(phone);
        if(error != null){
            model.addAttribute("error", error);
            return "profile";
        }
        user.updateDetails(name, phone);
        userService.save(user);
        model.addAttribute("currentUser", user);
        model.addAttribute("message", "Your details are saved.");
        return "profile";
    }

    @PostMapping("/change-password")
    public String ChangePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 HttpSession session,
                                 Model model){
        User user = (User) session.getAttribute("user");
        if(user == null){
            return "redirect:/login";
        }
        String error = userService.checkPasswordRules(newPassword);
        if(error != null){
            model.addAttribute("error", error);
            return "profile";
        }
        if(user.changePassword(oldPassword, newPassword)){
            userService.save(user);
            model.addAttribute("message", "Password changed.");
        } else {
            model.addAttribute("error", "Old password is wrong.");
        }
        return "profile";
    }
}