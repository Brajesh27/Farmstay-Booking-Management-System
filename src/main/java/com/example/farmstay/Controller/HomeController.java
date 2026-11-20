package com.example.farmstay.Controller;



import com.example.farmstay.Service.FarmstayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {
    private FarmstayService service;
    public HomeController(FarmstayService service){
        this.service = service;
    }
    @GetMapping("/")
    public String home(@RequestParam(required = false) String location,
                       @RequestParam(required = false) Double maxPrice,
                       @RequestParam(required = false) Integer guests,
                       Model model){
        model.addAttribute("farmstays", service.search(location, maxPrice, guests));
        // so the search boxes keep what the user typed
        model.addAttribute("location", location);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("guests", guests);
        return "index";
    }
}
