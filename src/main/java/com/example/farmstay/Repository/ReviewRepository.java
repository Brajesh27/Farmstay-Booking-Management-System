package com.example.farmstay.Repository;

import com.example.farmstay.Model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
}
