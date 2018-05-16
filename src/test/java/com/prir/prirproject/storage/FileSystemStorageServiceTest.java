package com.prir.prirproject.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class FileSystemStorageServiceTest {

    final Path root = Paths.get("upload-dir");

    FileSystemStorageService service;

    @Before
    public void init() {
        service = new FileSystemStorageService();
        service.init();
    }

    @Test
    public void initTest() {
        service.init();
    }

    @Test
    public void smokeTest() {
        assertThat(Files.exists(root)).isTrue();
    }

    @Test
    public void loadNonExistent() {
        assertThat(service.load("nonExistentFile.txt")).doesNotExist();
    }

    @Test(expected = FileNotFoundException.class)
    public void loadNonExistentExpectedFileNotFound() {
        service.loadAsResource("file.txt");
    }

    @Test
    public void saveAndLoad() {
        service.store(new MockMultipartFile("file",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Content".getBytes()));

        assertThat(service.load("file.txt")).exists();
        assertThat(service.load("file.txt")).isEqualTo(Paths.get(root + "/file.txt"));
    }

    @Test
    public void saveAndLoadAsResource() {
        service.store(new MockMultipartFile("file",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Content".getBytes()));

        assertThat(service.loadAsResource("file.txt")).isNotNull();
        assertThat(service.loadAsResource("file.txt")).isInstanceOf(Resource.class);
    }

    @Test(expected = StorageException.class)
    public void saveNotPermitted() {
        service.store(new MockMultipartFile("file",
                "../file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Content".getBytes()));
    }

    @After
    public void clean() throws Exception {
        FileSystemUtils.deleteRecursively(root);
    }

}
