package com.example.farmstay.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
@Document("bookings")
public class Booking {
    @Id
    private String id;
    @DBRef
    private User user;
    @DBRef
    private Farmstay farmstay;
    private LocalDate CheckIn;
    private LocalDate CheckOut;
    private int guests;
    private String status;
    public Booking(String id, User user, Farmstay farmstay, LocalDate CheckIn, LocalDate CheckOut, int guests){
        this.id =id;
        this.user = user;
        this.farmstay = farmstay;
        this.CheckIn = CheckIn;
        this.CheckOut = CheckOut;
        this.guests = guests;
        this.status = "CONFIRMED";
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Farmstay getFarmstay() {
        return farmstay;
    }

    public int getGuests() {
        return guests;
    }

    public LocalDate getCheckIn() {
        return CheckIn;
    }

    public LocalDate getCheckOut() {
        return CheckOut;
    }

    public User getUser() {
        return user;
    }
    public long getNights(){
        return ChronoUnit.DAYS.between(CheckIn,CheckOut);
    }
    public double getTotalPrice(){
        return getNights()* farmstay.getPricePerNight();
    }
    public boolean isActive(){
        return status.equals("CONFIRMED");
    }
    public void cancel(){
        status = "CANCELLED";
    }
    // can only cancel before the stay starts
    public boolean canCancel(){
        return isActive() && CheckIn.isAfter(LocalDate.now());
    }

    // the stay is over (used to show "Completed" instead of a Cancel button)
    public boolean isCompleted(){
        return isActive() && !CheckOut.isAfter(LocalDate.now());
    }
    // what the Status column shows: COMPLETED once the stay is over
    public String getDisplayStatus(){
        if(isCompleted()){
            return "COMPLETED";
        }
        return status;
    }
}
