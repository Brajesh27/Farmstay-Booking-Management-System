package com.example.farmstay.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
@ControllerAdvice
public class CurrentUserAdvice {
    @ModelAttribute
    public void addCurrentUser(HttpSession session, Model model){
        model.addAttribute("currentUser",session.getAttribute("user"));
    }
}
