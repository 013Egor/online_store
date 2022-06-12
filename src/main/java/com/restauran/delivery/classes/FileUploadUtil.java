package com.restauran.delivery.classes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

 @Service
public class FileUploadUtil {
    
    String directory;

    public FileUploadUtil() {
        directory = "src/main/resources/static/img/";
    }

    public FileUploadUtil(String directory) {
        this.directory = directory;
    }

    public void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException {
                
        Path uploadPath = Paths.get(directory + uploadDir);
         
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        }      
    }

    public void deleteFiles(int id) throws IOException {
        Path dir = Paths.get(directory+id);
        Files.walk(dir).map(Path::toFile).forEach(File::delete);     
    }
}