package com.prir.prirproject.parser;

import com.prir.prirproject.storage.StorageService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

@Service
public class ResultParserService {

    private StorageService storageService;
    private String SPECIAL_SIGN = "***";

    public ResultParserService(StorageService storageService) {
        this.storageService = storageService;
    }

    public String parseResult(String filename,
                              String pattern,
                              int patternLength,
                              List<Integer> positions) {

        Path file = storageService.load(filename);
        StringBuilder content = new StringBuilder();
        try {
            content.append(FileUtils.readFileToString(file.toFile(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int tmp = 0;
        for (int i=0; i<positions.size(); i++) {

            content.insert(positions.get(i) + tmp, SPECIAL_SIGN);
            tmp += SPECIAL_SIGN.length();
            content.insert(positions.get(i) + patternLength + tmp, SPECIAL_SIGN);
            tmp += SPECIAL_SIGN.length();
        }

        String resultFilename = FilenameUtils.removeExtension(filename) + "_" + pattern + ".txt";
        storageService.store(content.toString(),
                FilenameUtils.removeExtension(filename) + "_" + pattern + ".txt");

        return resultFilename;
    }

}
