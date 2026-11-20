package com.example.farmstay.Service;

import com.example.farmstay.Model.Booking;
import com.example.farmstay.Model.Farmstay;
import com.example.farmstay.Model.User;
import com.example.farmstay.Repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class BookingService {
    private BookingRepository repository;

    public BookingService(BookingRepository repository){
        this.repository = repository;
    }

    public boolean IsAvailable(Farmstay farmstay, LocalDate Checkin, LocalDate Checkout){
        for(Booking b : repository.findAll()){
            if(b.isActive() && b.getFarmstay() != null && b.getFarmstay().getId().equals(farmstay.getId())){
                if(Checkin.isBefore(b.getCheckOut()) && Checkout.isAfter(b.getCheckIn())){
                    return false;
                }
            }
        }
        return true;
    }

    public Booking book(User user, Farmstay farmstay, LocalDate CheckIn, LocalDate CheckOut, int guests){
        Booking b = new Booking(null, user, farmstay, CheckIn, CheckOut, guests);
        return repository.save(b);
    }

    public List<Booking> getBookingsOf(User user){
        List<Booking> result = new ArrayList<>();
        for(Booking b: repository.findAll()){
            if(b.getUser() != null && b.getUser().getUsername().equals(user.getUsername())){
                result.add(b);
            }
        }
        return result;
    }

    // every night that is already booked for this farmstay, as text like "2026-12-24"
    public List<String> getBookedNights(Farmstay farmstay){
        List<String> nights = new ArrayList<>();
        for(Booking b : repository.findAll()){
            if(b.isActive() && b.getFarmstay() != null && b.getFarmstay().getId().equals(farmstay.getId())){
                LocalDate day = b.getCheckIn();
                while(day.isBefore(b.getCheckOut())){
                    nights.add(day.toString());
                    day = day.plusDays(1);
                }
            }
        }
        return nights;
    }

    // the admin can cancel any booking
    public void cancelByAdmin(String bookingId){
        Booking b = repository.findById(bookingId).orElse(null);
        if(b != null && b.canCancel()){
            b.cancel();
            repository.save(b);
        }
    }

    // used when the admin deletes a farmstay
    public void deleteBookingsOf(Farmstay farmstay){
        for(Booking b : repository.findAll()){
            if(b.getFarmstay() == null || b.getFarmstay().getId().equals(farmstay.getId())){
                repository.delete(b);
            }
        }
    }

    // true if this guest already finished a stay at this farmstay
    public boolean hasCompletedStay(User user, Farmstay farmstay){
        for(Booking b : getBookingsOf(user)){
            if(b.isActive() && b.getFarmstay() != null
                    && b.getFarmstay().getId().equals(farmstay.getId())
                    && !b.getCheckOut().isAfter(LocalDate.now())){
                return true;
            }
        }
        return false;
    }

    // ---------- admin dashboard ----------

    public int countByStatus(String status){
        int count = 0;
        for(Booking b : repository.findAll()){
            if(status.equals(b.getStatus())){
                count++;
            }
        }
        return count;
    }

    // money from confirmed bookings only
    public double getTotalRevenue(){
        double total = 0;
        for(Booking b : repository.findAll()){
            if(b.isActive() && b.getFarmstay() != null){
                total += b.getTotalPrice();
            }
        }
        return total;
    }

    // confirmed bookings that check in this month
    public int countThisMonth(){
        LocalDate today = LocalDate.now();
        int count = 0;
        for(Booking b : repository.findAll()){
            if(b.isActive() && b.getCheckIn().getMonth() == today.getMonth()
                    && b.getCheckIn().getYear() == today.getYear()){
                count++;
            }
        }
        return count;
    }

    public String getMostBookedFarmstay(){
        Map<String, Integer> counts = new HashMap<>();
        for(Booking b : repository.findAll()){
            if(b.isActive() && b.getFarmstay() != null){
                String name = b.getFarmstay().getName();
                counts.put(name, counts.getOrDefault(name, 0) + 1);
            }
        }
        String best = "-";
        int bestCount = 0;
        for(String name : counts.keySet()){
            if(counts.get(name) > bestCount){
                best = name;
                bestCount = counts.get(name);
            }
        }
        return best;
    }

    // ---------- admin filter ----------
    // empty farmstayId or status means "all"
    public List<Booking> filter(String farmstayId, String status, boolean upcomingOnly){
        List<Booking> result = new ArrayList<>();
        for(Booking b : repository.findAll()){
            if(!farmstayId.isEmpty() && (b.getFarmstay() == null || !b.getFarmstay().getId().equals(farmstayId))){
                continue;
            }
            if(!status.isEmpty() && !status.equals(b.getStatus())){
                continue;
            }
            if(upcomingOnly && b.getCheckIn().isBefore(LocalDate.now())){
                continue;
            }
            result.add(b);
        }
        return result;
    }

    public List<Booking> getAll(){
        return repository.findAll();
    }

    public void cancel(String BookingId, User user){
        for(Booking b: repository.findAll()){
            if(b.getUser() != null && b.getId().equals(BookingId)
                    && b.getUser().getUsername().equals(user.getUsername())){
                if(b.canCancel()){
                    b.cancel();
                    repository.save(b);
                }
            }
        }
    }
}