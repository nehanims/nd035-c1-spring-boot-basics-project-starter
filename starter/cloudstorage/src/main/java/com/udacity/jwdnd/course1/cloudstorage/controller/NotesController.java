package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.CloudStorageApplicationException;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

import static com.udacity.jwdnd.course1.cloudstorage.util.Message.*;

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

    @PostMapping("/save-note")
    public String addNote(NoteForm noteForm, Authentication authentication, RedirectAttributes redirectAttributes) throws CloudStorageApplicationException {

        Integer loggedInUserId = userService.getLoggedInUserId(authentication);
        validateOperation(noteForm, noteForm.getNoteId(), loggedInUserId);
        notesService.addNote(noteForm, loggedInUserId);
        redirectAttributes.addFlashAttribute("successMessage", NOTE_SAVED_SUCCESSFULLY);
        return "redirect:/home";
    }

    @GetMapping("/delete-note/{id}")
    public String deleteNote(@PathVariable("id") Integer noteId, Authentication authentication, Model model, RedirectAttributes redirectAttributes) throws CloudStorageApplicationException {
        validateOperation(null, noteId, userService.getLoggedInUserId(authentication));
        notesService.deleteNote(noteId);
        redirectAttributes.addFlashAttribute("successMessage", NOTE_DELETED_SUCCESSFULLY);

        return "redirect:/home";
    }

    @ModelAttribute
    public void addModelAttribute(){
        navigationService.setSelectedTab(Tab.NOTES);
    }

    private void validateOperation(NoteForm noteForm, Integer noteId, Integer loggedInUserId) throws CloudStorageApplicationException {
        if(noteId !=null) {
            Notes note = notesService.getNoteByNoteId(noteId);
            if(note==null)
                throw new CloudStorageApplicationException(NOTE_DOES_NOT_EXIST);
            if (!note.getUserId().equals(loggedInUserId))
                throw new CloudStorageApplicationException(OPERATION_NOT_ALLOWED);
        }

        if(noteForm!=null){
            if(noteForm.getNoteDescription().length()>100)
                throw new CloudStorageApplicationException(NOTE_DESCRIPTION_EXCEEDS_LIMIT);

            boolean isDuplicateNote = notesService.getNotes(loggedInUserId)
                    .stream()
                    .anyMatch(noteForm1 -> noteForm1.getNoteTitle().equals(noteForm.getNoteTitle()) && noteForm1.getNoteDescription().equals(noteForm.getNoteDescription()));
            if(isDuplicateNote)
                throw new CloudStorageApplicationException(DUPLICATE_NOTE);
        }
    }
}
