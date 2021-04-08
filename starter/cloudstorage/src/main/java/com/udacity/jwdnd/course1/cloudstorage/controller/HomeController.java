package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;

@Controller
public class HomeController {

    private final UserService userService;
    private final NotesService notesService;
    private final FileManagementService fileService;
    private final CredentialsService credentialsService;
    private final NavigationService navigationService;

    public HomeController(UserService userService, NotesService notesService, FileManagementService fileService, CredentialsService credentialsService, NavigationService navigationService) {
        this.userService = userService;
        this.notesService = notesService;
        this.fileService = fileService;
        this.credentialsService = credentialsService;
        this.navigationService = navigationService;
    }

    @GetMapping("/home")
    public String getHomePage(NoteForm noteForm){
        return "home";
    }

    @ModelAttribute
    public void addModelAttribute(Authentication authentication, Model model){
        final Integer loggedInUserId = userService.getLoggedInUserId(authentication);
        model.addAttribute("notes", notesService.getNotes(loggedInUserId));
        model.addAttribute("files", fileService.getFiles(loggedInUserId));
        model.addAttribute("credentials", credentialsService.getCredentials(loggedInUserId));
        model.addAttribute("selectedTab", navigationService.getSelectedTab());
    }
}
