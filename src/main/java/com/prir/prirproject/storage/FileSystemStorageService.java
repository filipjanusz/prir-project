package com.prir.prirproject.storage;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final String ROOT_LOCATION = "./upload-dir";

    private final Path root;

    public FileSystemStorageService() {
        this.root = Paths.get(ROOT_LOCATION);
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new StorageException("Failed to initialize storage", e);
        }
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                throw new StorageException("Cannot store file with relative path outside directory" + filename);
            }
            try (InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename);
        }
    }

    @Override
    public void store(String content, String filename) {
        try {
            if (Files.exists(root.resolve(filename))) {
                throw new StorageException("File " + filename + " already exist");
            }
            if (filename.contains("..")) {
                throw new StorageException("Cannot store file with relative path outside directory" + filename);
            }
            FileUtils.writeStringToFile(root.resolve(filename).toFile(), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.list(root).map(root::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return root.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Failed to read file " + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Failed to read file " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            Files.list(root).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file", e);
        }
    }
}
