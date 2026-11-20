package com.example.farmstay.Service;
import com.example.farmstay.Model.Farmstay;
import com.example.farmstay.Repository.FarmstayRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FarmstayService {
    private FarmstayRepository repository;

    public FarmstayService(FarmstayRepository repository) {
        this.repository = repository;
    }

    public List<Farmstay> GetAll(){
        return repository.findAll();
    }

    public Farmstay add(String name, double PricePernight, int maxguests, String location, String description){
        // id is null, so MongoDB creates a new id for us
        Farmstay f = new Farmstay(null, name, PricePernight, maxguests, location, description);
        return repository.save(f);
    }

    public Farmstay findbyID(String id){
        return repository.findById(id).orElse(null);
    }

    public List<Farmstay> search(String location, Double maxPrice, Integer guests){
        List<Farmstay> result = new ArrayList<>();
        for(Farmstay f : repository.findAll()){
            if(location != null && !location.isBlank()){
                String place = f.getLocation() == null ? "" : f.getLocation();
                if(!place.toLowerCase().contains(location.trim().toLowerCase())){
                    continue;
                }
            }
            if(maxPrice != null && f.getPricePerNight() > maxPrice){
                continue;
            }
            if(guests != null && f.getMaxGuests() < guests){
                continue;
            }
            result.add(f);
        }
        return result;
    }

    public void delete(Farmstay farmstay){
        repository.delete(farmstay);
    }

    public void save(Farmstay farmstay){
        repository.save(farmstay);
    }
}