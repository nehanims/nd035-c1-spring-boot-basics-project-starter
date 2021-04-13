package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.CloudStorageApplicationException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Tab;
import com.udacity.jwdnd.course1.cloudstorage.services.FileManagementService;
import com.udacity.jwdnd.course1.cloudstorage.services.NavigationService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.udacity.jwdnd.course1.cloudstorage.util.Message.*;

import java.io.IOException;

@Controller
public class FileManagementController {
    private final FileManagementService fileService;
    private final UserService userService;
    private final NavigationService navigationService;

    public FileManagementController(FileManagementService fileService, UserService userService, NavigationService navigationService) {
        this.fileService = fileService;
        this.userService = userService;
        this.navigationService = navigationService;
    }

    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication, RedirectAttributes redirectAttributes) throws IOException {

        fileService.uploadFile(multipartFile, userService.getLoggedInUserId(authentication));
        redirectAttributes.addFlashAttribute("successMessage", FILE_SAVED_SUCCESSFULLY);
        return "redirect:/home";
    }

    @GetMapping("/delete-file/{id}")
    public String deleteFile(@PathVariable("id") Integer fileId, Authentication authentication, RedirectAttributes redirectAttributes) throws CloudStorageApplicationException {
        validateOperation(fileId, userService.getLoggedInUserId(authentication));

        fileService.deleteFile(fileId);
        redirectAttributes.addFlashAttribute("successMessage", FILE_DELETED_SUCCESSFULLY);
        return "redirect:/home";
    }

    @GetMapping("/view-file/{id}")
    public ResponseEntity<Resource> getFile ( @PathVariable("id") Integer fileId, Authentication authentication) throws CloudStorageApplicationException {

        validateOperation(fileId, userService.getLoggedInUserId(authentication));
        Files file = fileService.getFileByFileId(fileId);
        //Referenced https://knowledge.udacity.com/questions/328879
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

    private void validateOperation(Integer fileId, Integer loggedInUserId) throws CloudStorageApplicationException {
        if(fileId !=null){
            Files file = fileService.getFileByFileId(fileId);
            if(file==null)
                throw new CloudStorageApplicationException(FILE_DOES_NOT_EXIST);
            if(!file.getUserId().equals(loggedInUserId))
                throw new CloudStorageApplicationException(OPERATION_NOT_ALLOWED);
        }
    }

    @ModelAttribute
    public void addModelAttribute() {
        navigationService.setSelectedTab(Tab.FILES);
    }
}
