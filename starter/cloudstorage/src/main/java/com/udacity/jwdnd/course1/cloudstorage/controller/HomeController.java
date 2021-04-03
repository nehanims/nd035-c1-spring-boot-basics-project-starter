package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileManagementService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;
    private final NotesService notesService;
    private final FileManagementService fileService;
    private final CredentialsService credentialsService;

    public HomeController(UserService userService, NotesService notesService, FileManagementService fileService, CredentialsService credentialsService) {
        this.userService = userService;
        this.notesService = notesService;
        this.fileService = fileService;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/home")
    public String getHomePage(NoteForm noteForm, Authentication authentication, Model model){
        final Integer loggedInUserId = userService.getLoggedInUserId(authentication);
        model.addAttribute("notes", notesService.getNotes(loggedInUserId));
        model.addAttribute("files", fileService.getFiles(loggedInUserId));
        model.addAttribute("credentials", credentialsService.getCredentials(loggedInUserId));
        return "home";
    }
}
