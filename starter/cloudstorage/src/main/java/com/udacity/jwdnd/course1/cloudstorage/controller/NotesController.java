package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Tab;
import com.udacity.jwdnd.course1.cloudstorage.services.NavigationService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NotesController {
    private final NotesService notesService;
    private final UserService userService;
    private final NavigationService navigationService;

    public NotesController(NotesService notesService, UserService userService, NavigationService navigationService) {
        this.notesService = notesService;
        this.userService = userService;
        this.navigationService = navigationService;
    }

    @GetMapping("/delete-note/{id}")
    public String deleteNote(@PathVariable("id") Integer noteId, Authentication authentication, Model model){
        notesService.deleteNote(noteId);
        return "redirect:/home";
    }

    @PostMapping("/save-note")
    public String addNote(NoteForm noteForm, Authentication authentication, Model model){
        notesService.addNote(noteForm, userService.getLoggedInUserId(authentication));
        return "redirect:/home";
    }

    //TODO What is the right way to do this?? Should this be implemented using aspects? Or maybe controller advice?: NO - I think flash attributes may be the correct way to do this since they survive redirects: https://knasmueller.net/how-to-set-a-flash-message-in-spring-boot-with-thymeleaf
    @ModelAttribute
    public void addModelAttribute(){
        navigationService.setSelectedTab(Tab.NOTES);
    }

}
