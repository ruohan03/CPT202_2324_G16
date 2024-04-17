package com.example.demo.Controller;

import com.example.demo.Model.Locker;
import com.example.demo.Service.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LockerController {
    @Autowired
    private LockerService lockerService;

    @GetMapping("/locker")
    public String showLockers(Model model) {
        List<Locker> lockers = lockerService.getAllLockers(); // Assuming this method exists
        model.addAttribute("lockers", lockers);
        return "lockerManagement";
    }

    @GetMapping("/reserveLocker")
    public String reserveLocker(@RequestParam String lockerId) {
        lockerService.reserveLocker(lockerId);
        return "redirect:/locker";  // Redirect to the locker management page to see updates
    }

    @GetMapping("/returnLocker")
    public String returnLocker(@RequestParam String lockerId) {
        lockerService.returnLocker(lockerId);
        return "redirect:/locker";  // Redirect to the locker management page to see updates
    }
}
