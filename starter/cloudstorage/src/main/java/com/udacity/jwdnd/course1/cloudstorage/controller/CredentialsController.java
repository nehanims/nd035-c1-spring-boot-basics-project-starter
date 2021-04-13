package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.CloudStorageApplicationException;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
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

import static com.udacity.jwdnd.course1.cloudstorage.util.Message.*;

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
    public String saveCredentials(@ModelAttribute("credential") CredentialsForm credentialsForm, Authentication authentication, Model model, RedirectAttributes redirectAttributes) throws CloudStorageApplicationException {
        Integer loggedInUserId = userService.getLoggedInUserId(authentication);
        validateOperationAuthorized(credentialsForm, credentialsForm.getCredentialId(), loggedInUserId);

        credentialsService.saveCredentials(credentialsForm, userService.getLoggedInUserId(authentication));
        redirectAttributes.addFlashAttribute("successMessage", CREDENTIAL_SAVED_SUCCESSFULLY);
        return "redirect:/home";
    }
    
    @GetMapping("/delete-credential/{id}")
    public String deleteCredentials(@PathVariable("id") Integer credentialId, Authentication authentication, RedirectAttributes redirectAttributes) throws CloudStorageApplicationException {
        validateOperationAuthorized(null, credentialId, userService.getLoggedInUserId(authentication));

        credentialsService.deleteCredentials(credentialId);
        redirectAttributes.addFlashAttribute("successMessage", CREDENTIAL_DELETED_SUCCESSFULLY);
        return "redirect:/home";
    }

    @ModelAttribute
    public void addModelAttribute(){
        navigationService.setSelectedTab(Tab.CREDENTIALS);
    }


    private void validateOperationAuthorized(CredentialsForm credentialsForm, Integer credentialId, Integer loggedInUserId) throws CloudStorageApplicationException {
        if(credentialId !=null) {
            Credentials credential = credentialsService.getCredentialByCredentialId(credentialId);
            if(credential==null)
                throw new CloudStorageApplicationException(CREDENTIAL_DOES_NOT_EXIST);
            if (!credential.getUserId().equals(loggedInUserId))
                throw new CloudStorageApplicationException(OPERATION_NOT_ALLOWED);
        }

        if(credentialsForm!=null){
            boolean isDuplicateCredential = credentialsService.getCredentials(loggedInUserId)
                    .stream()
                    .anyMatch(credentialsForm1 -> credentialsForm1.getUrl().equals(credentialsForm.getUrl()) && credentialsForm1.getUsername().equals(credentialsForm.getUsername()));
            if(isDuplicateCredential)
                throw new CloudStorageApplicationException(DUPLICATE_CREDENTIAL);
        }
    }
}
