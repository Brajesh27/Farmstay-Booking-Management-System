package com.example.farmstay.Controller;

import com.example.farmstay.Model.Farmstay;
import com.example.farmstay.Model.User;
import com.example.farmstay.Service.FarmstayService;
import com.example.farmstay.Service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewController {
    private ReviewService reviewService;
    private FarmstayService farmstayService;

    public ReviewController(ReviewService reviewService, FarmstayService farmstayService) {
        this.reviewService = reviewService;
        this.farmstayService = farmstayService;
    }

    @PostMapping("/review")
    public String AddReview(@RequestParam String farmstayId,
                            @RequestParam int rating,
                            @RequestParam(defaultValue = "") String comment,
                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Farmstay farmstay = farmstayService.findbyID(farmstayId);
        if (farmstay == null) {
            return "redirect:/";
        }
        // keep the rating between 1 and 5
        if (rating < 1) rating = 1;
        if (rating > 5) rating = 5;

        if (reviewService.canReview(user, farmstay)) {
            reviewService.addReview(user, farmstay, rating, comment);
        }
        return "redirect:/farmstay/" + farmstayId;
    }
}
