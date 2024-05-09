package org.vagabond.common.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.vagabond.common.user.UserEntity;
import org.vagabond.engine.crud.repository.BaseRepository;
import org.vagabond.engine.crud.service.BaseService;
import org.vagabond.engine.exeption.MetierException;

@ApplicationScoped
public class FileService extends BaseService<FileEntity> {

    @ConfigProperty(name = "upload.directory", defaultValue = "/tmp")
    private String uploadDirectory;

    @Inject
    FileRepository fileRepository;

    public String uploadFile(FormValue file, String directory, Long id) {
        return uploadFile(file, String.format("%s/%s", directory, id));
    }

    public String uploadFile(FormValue file, String directory) {
        String fileName = file.getFileName();
        String updloadDirectory = String.format("%s/%s", uploadDirectory, directory);
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
        return String.format("/%s/%s", directory, fileName);
    }

    public File downloadFile(String fileName) {
        Path path = Paths.get(uploadDirectory + fileName);
        return path.toFile();
    }

    public FileEntity create(String name, String directory, String path, UserEntity user) {
        FileEntity file = new FileEntity();
        file.name = name;
        file.directory = directory;
        file.path = path;
        file.user = user;
        return persist(file);
    }

    public FileEntity findByFilename(String filename, Long userId) {
        var files = findBy("where name = ?1 and user.id = ?2", filename, userId);
        return files.isEmpty() ? null : files.get(0);
    }

    @Override
    public BaseRepository<FileEntity> getRepository() {
        return fileRepository;
    }

}