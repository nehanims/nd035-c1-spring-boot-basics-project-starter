package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String getUserRegistrationForm(Authentication authentication, User user, Model mode){
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            return "signup";
        else return "redirect:/home";
    }

    @PostMapping("/signup")
    public String registerNewUser(User user, Model model, RedirectAttributes redirectAttributes) {//TODO use a UserForm instead of the actual Entity class

        if(userService.isUsernameAvailable(user.getUsername())) {
            user = userService.registerNewUser(user);
        }
        else {
            model.addAttribute("usernameUnavailableError", "Username unavailable");//TODO is this the best way to handle this on the front end? - NO - use the error.html tags with flash attributes :https://knasmueller.net/how-to-set-a-flash-message-in-spring-boot-with-thymeleaf
            user.setUsername("");
            return "signup";
        }
        model.addAttribute("user", user);

        redirectAttributes.addFlashAttribute("successfulSignup", true);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginForm(Authentication authentication){
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/home";
    }

}
