package by.bsuir.controllers;

import by.bsuir.entity.User;
import by.bsuir.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User emptyUser() {
        return new User();
    }

    @GetMapping("/register")
    public String registrationPage() {
        return "registration_page";
    }

    @PostMapping("/register")
    public String register(User user) {
        user.setPassword(user.getPassword().trim());
        if (userService.findByUsername(user.getUsername()) != null || user.getPassword().length() < 4) {
            return "registration_page";
        }
        userService.save(user);
        return "redirect:/login";
    }

}
