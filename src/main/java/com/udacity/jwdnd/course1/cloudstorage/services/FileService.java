package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    public List<File> getFiles(Integer userId) {
        return fileMapper.findByUserId(userId);
    }

    public boolean isFileNameAvailable(String filename, Integer userId) {
        return fileMapper.findByFilenameAndUserId(filename, userId) == null;
    }

    public void addFile(MultipartFile file, Integer userId) throws IOException {
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        String fileSize = String.valueOf(file.getSize());
        byte[] fileData = file.getBytes();

        File newFile = new File(null, filename, contentType, fileSize, userId, fileData);
        fileMapper.insert(newFile);
    }

    public void deleteFile(Integer fileId) {
        fileMapper.delete(fileId);
    }

    public File getFile(Integer fileId, Integer userId) {
        return fileMapper.getFile(fileId, userId);
    }
}
