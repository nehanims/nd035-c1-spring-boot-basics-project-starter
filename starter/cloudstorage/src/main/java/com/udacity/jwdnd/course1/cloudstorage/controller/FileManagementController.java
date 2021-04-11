package com.udacity.jwdnd.course1.cloudstorage.controller;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    //TODO add the parameters for the Home controller (probably only a list of files for this user)
    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication, Model model){
        fileService.uploadFile(multipartFile, userService.getLoggedInUserId(authentication));
        return "redirect:/home";
    }

    @GetMapping("/delete-file/{id}")
    public String deleteFile(@PathVariable("id") Integer fileId) {
        //TODO before deleting or updating check if the current logged in user has the right to send the request, i.e delete this file,
        // in fact check this for all types of updates and deletes,
        // you could add a check in the update and delete for each file, note and credential query itself i.e. add to the where clause userid=loggedinuserid,
        // and then if no rows are updated or deleted then return failure response up through the service layer to the controller

        //TODO don't allow upload of existing filename,
        //TODO don't allow upload of empty file
        //TODO how to handle multiple file upload?

        fileService.deleteFile(fileId);
        return "redirect:/home";
    }

    @GetMapping("/view-file/{id}")//TODO change these end point mappings to be more RESTful e.g. /file/view/{fileId}
    public ResponseEntity<Resource> getFile ( @PathVariable("id") Long fileId, Authentication authentication)
    {
        Files file = fileService.getFileByFileId(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                + file.getFilename() + "\"").body(new
                        ByteArrayResource(file.getFileData()));
    }

    @ModelAttribute
    public void addModelAttribute(){
        navigationService.setSelectedTab(Tab.FILES);
    }
}
