package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Tab;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.NavigationService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialsController {
    private final CredentialsService credentialsService;
    private final UserService userService;
    private final NavigationService navigationService;

    public CredentialsController(CredentialsService credentialsService, UserService userService, NavigationService navigationService) {
        this.credentialsService = credentialsService;
        this.userService = userService;
        this.navigationService = navigationService;
    }

    @PostMapping("/save-credential")
    public String saveCredentials(@ModelAttribute("credential") CredentialsForm credentialsForm, Authentication authentication, Model model, RedirectAttributes redirectAttributes){
        credentialsService.saveCredentials(credentialsForm, userService.getLoggedInUserId(authentication));
        redirectAttributes.addFlashAttribute("successMessage", "SUCCESS: Credential saved successfully");
        return "redirect:/home";
    }
    
    @GetMapping("/delete-credential/{id}")
    public String deleteCredentials(@PathVariable("id") Integer credentialId, RedirectAttributes redirectAttributes){
        credentialsService.deleteCredentials(credentialId);
        redirectAttributes.addFlashAttribute("successMessage", "SUCCESS: Credential deleted successfully");
        return "redirect:/home";
    }

    @ModelAttribute
    public void addModelAttribute(){
        navigationService.setSelectedTab(Tab.CREDENTIALS);
    }
}
