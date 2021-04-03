package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialsController {
    private final CredentialsService credentialsService;
    private final UserService userService;

    public CredentialsController(CredentialsService credentialsService, UserService userService) {
        this.credentialsService = credentialsService;
        this.userService = userService;
    }

    @PostMapping("/save-credential")//TODO rename to add-update
    public String saveCredentials(@ModelAttribute("credential") CredentialsForm credentialsForm, Authentication authentication, Model model){
        credentialsService.saveCredentials(credentialsForm, userService.getLoggedInUserId(authentication));
        return "redirect:/home";
    }
    
    @GetMapping("/delete-credential/{id}")
    public String deleteCredentials(@PathVariable("id") Integer credentialId){
        credentialsService.deleteCredentials(credentialId);
        return "redirect:/home";
    }
    
}
