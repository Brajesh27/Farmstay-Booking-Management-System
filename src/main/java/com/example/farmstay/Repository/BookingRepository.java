package com.example.farmstay.Repository;

import com.example.farmstay.Model.Booking;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking,String> {
}
