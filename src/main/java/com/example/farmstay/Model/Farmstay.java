package com.example.farmstay.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Document("farmstays")
public class Farmstay {
    private String id;
    private String name;
    private double pricePerNight;
    private int maxGuests;
    private String location;
    private String description;
    private List<String> photos = new ArrayList<>();

    public Farmstay(String id, String name, double pricePerNight, int maxGuests, String location, String description) {
        this.id = id;
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.location = location;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPricePerNight() { return pricePerNight; }
    public int getMaxGuests() { return maxGuests; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public List<String> getPhotos() { return photos; }

    public void addPhoto(String photoPath) {
        photos.add(photoPath);
    }

    public String getMainPhoto() {
        if (photos.isEmpty()) {
            return "";
        }
        return photos.get(0);
    }

    public void updateDetails(String name, double pricePerNight, int maxGuests, String location, String description) {
        this.name = name;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.location = location;
        this.description = description;
    }

    public void removePhoto(String photoPath){
        photos.remove(photoPath);
    }

}