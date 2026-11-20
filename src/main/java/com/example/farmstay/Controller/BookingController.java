package com.example.farmstay.Controller;

import com.example.farmstay.Model.Farmstay;
import com.example.farmstay.Model.User;
import com.example.farmstay.Service.BookingService;
import com.example.farmstay.Service.FarmstayService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
@Controller
public class BookingController {
    private BookingService bookingService;
    private FarmstayService farmstayService;
    public BookingController(BookingService bookingService, FarmstayService farmstayService){
        this.bookingService = bookingService;
        this.farmstayService = farmstayService;
    }

    @PostMapping("/book")
    public String book(@RequestParam String farmstayId,
                       @RequestParam String checkIn,
                       @RequestParam String checkOut,
                       @RequestParam int guests,
                       HttpSession session,
                       RedirectAttributes redirect){
        User user = (User) session.getAttribute("user");
        if(user==null){
            return "redirect:/login";
        }
        if(user.isAdmin()){
            return "redirect:/";   // the owner does not book farmstays
        }
        Farmstay farmstay = farmstayService.findbyID(farmstayId);
        if(farmstay == null){
            return "redirect:/";
        }
        LocalDate in  = LocalDate.parse(checkIn);
        LocalDate out  = LocalDate.parse(checkOut);
        String err = null;
        if(in.isBefore(LocalDate.now())){
            err = "Check-in date cannot be in the past.";
        }
        else if(!out.isAfter(in)){
            err = "Check-out must be after check-in.";
        }
        else if(guests > farmstay.getMaxGuests()){
            err = "This farmstay allows up to " + farmstay.getMaxGuests() + " guests.";
        } else if (!bookingService.IsAvailable(farmstay, in, out)) {
            err = "Sorry, this farmstay is already booked for those dates.";
        }
        if(err!=null){
            // go back to the farmstay page and show the error there
            redirect.addFlashAttribute("error", err);
            return "redirect:/farmstay/" + farmstayId;
        }
        bookingService.book(user,farmstay,in,out,guests);
        return "redirect:/my-bookings";
    }

    @GetMapping("/my-bookings")
    public String MyBookings(HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        if(user==null){
            return "redirect:/login";
        }
        if(user.isAdmin()){
            return "redirect:/admin/bookings";
        }
        model.addAttribute("bookings", bookingService.getBookingsOf(user));
        return "my-bookings";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable String id, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user==null){
            return "redirect:/login";
        }
        bookingService.cancel(id,user);
        return "redirect:/my-bookings";
    }

    @PostMapping("/admin/cancel/{id}")
    public String AdminCancel(@PathVariable String id, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null || !user.isAdmin()){
            return "redirect:/login";
        }
        bookingService.cancelByAdmin(id);
        return "redirect:/admin/bookings";
    }

    @GetMapping("/admin/bookings")
    public String AllBookings(@RequestParam(defaultValue = "") String farmstayId,
                              @RequestParam(defaultValue = "") String status,
                              @RequestParam(defaultValue = "false") boolean upcoming,
                              HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user == null ||  !user.isAdmin()){
            return "redirect:/login";
        }
        // filtered table
        model.addAttribute("bookings", bookingService.filter(farmstayId, status, upcoming));
        model.addAttribute("farmstays", farmstayService.GetAll());
        model.addAttribute("selectedFarmstay", farmstayId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("upcoming", upcoming);

        // dashboard numbers (always for ALL bookings)
        model.addAttribute("confirmedCount", bookingService.countByStatus("CONFIRMED"));
        model.addAttribute("cancelledCount", bookingService.countByStatus("CANCELLED"));
        model.addAttribute("thisMonthCount", bookingService.countThisMonth());
        model.addAttribute("revenue", bookingService.getTotalRevenue());
        model.addAttribute("mostBooked", bookingService.getMostBookedFarmstay());
        return "admin-bookings";
    }

}