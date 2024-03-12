package org.vagabond.engine.crud.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.vagabond.engine.exeption.MetierException;

@ApplicationScoped
public class UploadService {

    @ConfigProperty(name = "upload.directory", defaultValue = "/")
    private String uploadDirectory;

    public String uploadFile(FormValue file, String directory, Long id) {
        String fileName = file.getFileName();
        String updloadDirectory = String.format("%s/%s/%s", uploadDirectory, directory, id);
        Path uploadPath = Paths.get(updloadDirectory);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException exception) {
                throw new MetierException("Error on création directory : " + uploadPath, exception);
            }
        }
        Path filePath = uploadPath.resolve(fileName);
        try (InputStream inputStream = file.getFileItem().getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new MetierException("Could not save file: " + fileName, exception);
        }
        return String.format("%s/%s/%s", directory, id, fileName);
    }

    public File downloadFile(String fileName) {
        Path path = Paths.get(uploadDirectory + fileName);
        return path.toFile();
    }

}