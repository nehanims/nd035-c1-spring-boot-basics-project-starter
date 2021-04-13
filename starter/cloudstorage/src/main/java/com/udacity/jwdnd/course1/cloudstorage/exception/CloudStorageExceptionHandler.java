package com.udacity.jwdnd.course1.cloudstorage.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CloudStorageExceptionHandler{

    @ExceptionHandler(CloudStorageApplicationException.class)
    public RedirectView handleApplicationException(CloudStorageApplicationException applicationException, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        RedirectView redirectView = new RedirectView("/home");
        RequestContextUtils.getOutputFlashMap(request)
                .put("saveErrorMessage", applicationException.getMessage());
        return redirectView;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public RedirectView handleMaxSizeException(MaxUploadSizeExceededException exception, HttpServletRequest request, HttpServletResponse response) {
        RedirectView redirectView = new RedirectView("/home");
        RequestContextUtils.getOutputFlashMap(request)
                .put("saveErrorMessage", "ERROR: File upload error. File exceeded size limit.");
        return redirectView;
    }

    @ExceptionHandler(IOException.class)
    public RedirectView handleIOException(IOException exception, HttpServletRequest request, HttpServletResponse response) {
        RedirectView redirectView = new RedirectView("/home");
        RequestContextUtils.getOutputFlashMap(request)
                .put("saveErrorMessage", "ERROR: File upload error." + exception.getMessage());
        return redirectView;
    }

}
