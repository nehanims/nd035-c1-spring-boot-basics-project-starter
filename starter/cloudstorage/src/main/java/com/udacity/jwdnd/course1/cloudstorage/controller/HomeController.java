package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
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

    public HomeController(UserService userService, NotesService notesService, FileManagementService fileService) {
        this.userService = userService;
        this.notesService = notesService;
        this.fileService = fileService;
    }

    @GetMapping("/home")
    public String getHomePage(NoteForm noteForm, Authentication authentication, Model model){
        final Integer loggedInUserId = userService.getLoggedInUserId(authentication);
        model.addAttribute("notes", notesService.getNotes(loggedInUserId));
        model.addAttribute("files", fileService.getFiles(loggedInUserId));//TODO probably shouldn't just send the whole file content as byte array until the view button is clicked
        return "home";
    }
}
