package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileManagementService {
    private final FileMapper fileMapper;

    public FileManagementService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void uploadFile(MultipartFile multipartFile, Integer userId) throws IOException {
            fileMapper.saveFile(Files.builder()
                    .filename(multipartFile.getOriginalFilename())
                    .contentType(multipartFile.getContentType())
                    .fileSize(((Long)multipartFile.getSize()).toString())
                    .userId(userId)
                    .fileData(multipartFile.getBytes())
                    .build());
    }

    public List<FileForm> getFiles(Integer loggedInUserId) {
        return fileMapper.getAllFiles(loggedInUserId)
                .stream()
                .map(this::getFileForm)
                .collect(Collectors.toList());
    }

    private FileForm getFileForm(Files file) {
        return FileForm.builder()
                .fileId(file.getFileId())
                .filename(file.getFilename())
                .fileSize(file.getFileSize())
                .contentType(file.getContentType())
                .userId(file.getUserId())
                .build();
    }

    public void deleteFile(Integer fileId) {
        fileMapper.deleteFile(fileId);
    }

    public Files getFileByFileId(Integer fileId) {
        return fileMapper.getFile(fileId);
    }
}
