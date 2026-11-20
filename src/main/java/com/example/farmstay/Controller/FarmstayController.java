package com.example.farmstay.Controller;

import com.example.farmstay.Model.User;
import com.example.farmstay.Service.BookingService;
import com.example.farmstay.Service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.example.farmstay.Model.Farmstay;
import com.example.farmstay.Service.FarmstayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class FarmstayController {
    private FarmstayService service;
    private BookingService bookingService;
    private ReviewService reviewService;

    public FarmstayController(FarmstayService service, BookingService bookingService, ReviewService reviewService){
        this.service = service;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
    }

    @GetMapping("/add")
    public String ShowAddForm(HttpSession session){
        if(!IsAdmin(session)){
            return "redirect:/login";
        }
        return "add";
    }

    @PostMapping("/add")
    public String AddFarmStay(@RequestParam String name,
                              @RequestParam double pricePerNight,
                              @RequestParam int maxGuests,
                              @RequestParam(defaultValue = "") String location,
                              @RequestParam(defaultValue = "") String description,
                              @RequestParam List<MultipartFile> photos, HttpSession session) throws IOException {
        if(!IsAdmin(session)){
            return "redirect:/login";
        }
        Farmstay farmstay = service.add(name, pricePerNight, maxGuests, location, description);
        savePhotos(farmstay, photos);
        service.save(farmstay);
        return "redirect:/";
    }

    @GetMapping("/farmstay/{id}")
    public String ShowDetails(@PathVariable String id, HttpSession session, Model model){
        Farmstay farmstay = service.findbyID(id);
        if (farmstay == null) {
            return "redirect:/";
        }
        model.addAttribute("farmstay", farmstay);
        model.addAttribute("bookedNights", bookingService.getBookedNights(farmstay));

        // reviews
        User user = (User) session.getAttribute("user");
        model.addAttribute("reviews", reviewService.getReviewsOf(farmstay));
        model.addAttribute("averageRating", reviewService.getAverageRating(farmstay));
        model.addAttribute("canReview", reviewService.canReview(user, farmstay));
        return "details";
    }

    @GetMapping("/edit/{id}")
    public String ShowEditForm(@PathVariable String id, HttpSession session, Model model){
        if(!IsAdmin(session)){
            return "redirect:/login";
        }
        Farmstay farmstay = service.findbyID(id);
        if (farmstay == null) {
            return "redirect:/";
        }
        model.addAttribute("farmstay", farmstay);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String EditFarmstay(@PathVariable String id,
                               @RequestParam String name,
                               @RequestParam double pricePerNight,
                               @RequestParam int maxGuests,
                               @RequestParam(defaultValue = "") String location,
                               @RequestParam(defaultValue = "") String description,
                               @RequestParam(required = false) List<MultipartFile> photos,
                               HttpSession session) throws IOException {
        if(!IsAdmin(session)){
            return "redirect:/login";
        }
        Farmstay farmstay = service.findbyID(id);
        if (farmstay == null) {
            return "redirect:/";
        }
        farmstay.updateDetails(name, pricePerNight, maxGuests, location, description);
        savePhotos(farmstay, photos);
        service.save(farmstay);
        return "redirect:/farmstay/" + id;
    }

    @PostMapping("/edit/{id}/remove-photo")
    public String RemovePhoto(@PathVariable String id, @RequestParam String photo, HttpSession session){
        if(!IsAdmin(session)){
            return "redirect:/login";
        }
        Farmstay farmstay = service.findbyID(id);
        if (farmstay != null) {
            farmstay.removePhoto(photo);
            service.save(farmstay);
        }
        return "redirect:/edit/" + id;
    }

    @PostMapping("/delete/{id}")
    public String DeleteFarmstay(@PathVariable String id, HttpSession session){
        if(!IsAdmin(session)){
            return "redirect:/login";
        }
        Farmstay farmstay = service.findbyID(id);
        if (farmstay != null) {
            bookingService.deleteBookingsOf(farmstay);   // its bookings go too
            reviewService.deleteReviewsOf(farmstay);     // and its reviews
            service.delete(farmstay);
        }
        return "redirect:/";
    }

    // saves each uploaded photo in the uploads folder and adds it to the farmstay
    private void savePhotos(Farmstay farmstay, List<MultipartFile> photos) throws IOException {
        if (photos == null) {
            return;
        }
        File folder = new File("uploads").getAbsoluteFile();
        folder.mkdirs();
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                String Filename = System.currentTimeMillis() + "_" + photo.getOriginalFilename().replace(" ", "_");
                photo.transferTo(new File(folder, Filename));
                farmstay.addPhoto("/uploads/" + Filename);
            }
        }
    }

    private boolean IsAdmin(HttpSession session){
        User user = (User) session.getAttribute("user");
        return user != null && user.isAdmin();
    }
}