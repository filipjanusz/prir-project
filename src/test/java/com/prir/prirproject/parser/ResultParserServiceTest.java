package com.prir.prirproject.parser;

import com.prir.prirproject.storage.FileSystemStorageService;
import com.prir.prirproject.storage.StorageService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultParserServiceTest {

    PdfParserService pdfParserService = new PdfParserService();
    StorageService storageService = new FileSystemStorageService();
    ResultParserService resultParserService = new ResultParserService(storageService);

    @Before
    public void init() throws Exception {
        storageService.init();
        File file = new File(Paths.get("./tests").resolve("test.pdf").toString());
        InputStream inputStream = new FileInputStream(file);
        pdfParserService.parsePdfAndStore(inputStream, file.getName());
    }

    @Test
    public void testResultParser() throws Exception {

        List<Integer> positions = Stream.of(6, 12)
                .collect(Collectors.toList());

        resultParserService.parseResult("test.txt", "ipsum", 5, positions);
        assertThat(storageService.load("test_ipsum.txt")).exists();
    }

    @After
    public void clean() throws Exception {
        FileSystemUtils.deleteRecursively(Paths.get("./upload-dir"));
    }

}
