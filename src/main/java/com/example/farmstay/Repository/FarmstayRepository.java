package com.example.farmstay.Repository;

import com.example.farmstay.Model.Farmstay;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FarmstayRepository extends MongoRepository<Farmstay,String> {
    
}
