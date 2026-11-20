package com.example.farmstay.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("reviews")
public class Review {
    @Id
    private String id;
    @DBRef
    private User user;
    @DBRef
    private Farmstay farmstay;
    private int rating;        // 1 to 5 stars
    private String comment;
    private LocalDate date;

    public Review(String id, User user, Farmstay farmstay, int rating, String comment, LocalDate date) {
        this.id = id;
        this.user = user;
        this.farmstay = farmstay;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public Farmstay getFarmstay() { return farmstay; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDate getDate() { return date; }

    // for example "★★★★☆" for 4 stars
    public String getStars() {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }
}
