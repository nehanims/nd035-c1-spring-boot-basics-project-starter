package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.FileManagementService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileManagementController {
    private final FileManagementService fileService;
    private final UserService userService;

    public FileManagementController(FileManagementService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    //TODO add the parameters for the Home controller (probably only a list of files for this user)
    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication, Model model){
        fileService.uploadFile(multipartFile, userService.getLoggedInUserId(authentication));
        return "redirect:/home";
    }
}
