package com.prir.prirproject.parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Service
public class PdfParserService {

    public void parsePdfAndStore(InputStream pdf, String filename) throws IOException {

        PDDocument pdfDocument = PDDocument.load(pdf);

        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();

            String text = pdfStripper.getText(pdfDocument);

            File tmpFile = new File(Paths
                    .get("./upload-dir")
                    .resolve(FilenameUtils.removeExtension(filename) + ".txt")
                    .toString());

            FileUtils.writeStringToFile(tmpFile, text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pdfDocument.close();
        }

    }

}
