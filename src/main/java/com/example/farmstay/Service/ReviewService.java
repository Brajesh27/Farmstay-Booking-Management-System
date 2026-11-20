package com.example.farmstay.Service;

import com.example.farmstay.Model.Farmstay;
import com.example.farmstay.Model.Review;
import com.example.farmstay.Model.User;
import com.example.farmstay.Repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    private ReviewRepository repository;
    private BookingService bookingService;

    public ReviewService(ReviewRepository repository, BookingService bookingService) {
        this.repository = repository;
        this.bookingService = bookingService;
    }

    public List<Review> getReviewsOf(Farmstay farmstay) {
        List<Review> result = new ArrayList<>();
        for (Review r : repository.findAll()) {
            if (r.getFarmstay() != null && r.getFarmstay().getId().equals(farmstay.getId())) {
                result.add(r);
            }
        }
        return result;
    }

    // 0 when there are no reviews yet
    public double getAverageRating(Farmstay farmstay) {
        List<Review> reviews = getReviewsOf(farmstay);
        if (reviews.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (Review r : reviews) {
            total += r.getRating();
        }
        return (double) total / reviews.size();
    }

    public boolean hasReviewed(User user, Farmstay farmstay) {
        for (Review r : getReviewsOf(farmstay)) {
            if (r.getUser() != null && r.getUser().getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    // only a guest who has finished a stay here, and not reviewed yet
    public boolean canReview(User user, Farmstay farmstay) {
        if (user == null || user.isAdmin()) {
            return false;
        }
        return bookingService.hasCompletedStay(user, farmstay) && !hasReviewed(user, farmstay);
    }

    public void addReview(User user, Farmstay farmstay, int rating, String comment) {
        repository.save(new Review(null, user, farmstay, rating, comment, LocalDate.now()));
    }

    // used when the admin deletes a farmstay
    public void deleteReviewsOf(Farmstay farmstay) {
        for (Review r : repository.findAll()) {
            if (r.getFarmstay() == null || r.getFarmstay().getId().equals(farmstay.getId())) {
                repository.delete(r);
            }
        }
    }
}
