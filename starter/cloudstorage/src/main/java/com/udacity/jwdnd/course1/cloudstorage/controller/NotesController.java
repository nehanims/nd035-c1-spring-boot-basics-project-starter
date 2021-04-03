package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NotesController {
    private final NotesService notesService;
    private final UserService userService;

    public NotesController(NotesService notesService, UserService userService) {
        this.notesService = notesService;
        this.userService = userService;
    }

    @GetMapping("/delete-note/{id}")
    public String deleteNote(@PathVariable("id") Integer noteId, Authentication authentication, Model model){
        notesService.deleteNote(noteId);
        return "redirect:/home";
    }

    //TODO rename to reflect that this is an "add-update-note" endpoint (and not just add-note)
    //TODO URL mappings look kinda weird the way you've implemented it(e.g. after you invoke delete-note or add-note, maybe you should re-direct to home page with the right tab selected, so that the home page URL is shown in the URL bar rather than the last operation)
    //TODO remember to either remove H2 DB persistence across server restarts from application.properties or use a different config for testing if needed - you could also just clear all data between tests if needed
    @PostMapping("/save-note")
    public String addNote(NoteForm noteForm, Authentication authentication, Model model){
        notesService.addNote(noteForm, userService.getLoggedInUserId(authentication));
        return "redirect:/home";
    }

}
