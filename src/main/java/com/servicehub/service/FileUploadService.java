package com.servicehub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {
    
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        
        String uploadPath = System.getProperty("user.dir") + File.separator + uploadDir;
        File uploadFolder = new File(uploadPath);
        
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File destination = new File(uploadPath + File.separator + fileName);
        
        file.transferTo(destination);
        
        return fileName;
    }
}