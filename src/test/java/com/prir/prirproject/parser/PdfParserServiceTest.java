package com.prir.prirproject.parser;


import com.prir.prirproject.storage.FileSystemStorageService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class PdfParserServiceTest {

    PdfParserService service = new PdfParserService();

    FileSystemStorageService storage = new FileSystemStorageService();

    @Before
    public void init() throws Exception {
        storage.init();
    }

    @Test
    public void pdfParserTest() throws Exception {

        File file = new File(Paths.get("./tests").resolve("test.pdf").toString());
        InputStream inputStream = new FileInputStream(file);

        service.parsePdfAndStore(inputStream, file.getName());

        assertThat(storage.load("test.txt")).exists();
    }

    @After
    public void clean() throws Exception {
        FileSystemUtils.deleteRecursively(Paths.get("./upload-dir"));
    }
}
