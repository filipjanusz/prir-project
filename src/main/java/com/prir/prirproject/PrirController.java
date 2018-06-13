package com.prir.prirproject;

import com.prir.prirproject.command.CommandExecutor;
import com.prir.prirproject.domain.RunParams;
import com.prir.prirproject.parser.PdfParserService;
import com.prir.prirproject.storage.FileNotFoundException;
import com.prir.prirproject.storage.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/prir/files")
public class PrirController {

    private final StorageService storageService;
    private final PdfParserService pdfParserService;

    public PrirController(StorageService storageService, PdfParserService pdfParserService) {
        this.storageService = storageService;
        this.pdfParserService = pdfParserService;
    }

    @GetMapping
    public String listUploadedFiles(Model model) {
        model.addAttribute("runParams", new RunParams());
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(PrirController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        model.addAttribute("filenames", storageService.loadAll()
                .filter(path -> path.toFile().getAbsolutePath().endsWith("txt"))
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {

        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String filename = file.getOriginalFilename();
        if (ext.equals("pdf") || ext.equals("txt")) {
            storageService.store(file);
            if (ext.equals("pdf")) {
                pdfParserService.parsePdfAndStore(file.getInputStream(), filename);
            }
            redirectAttributes.addFlashAttribute("info",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");

            return "redirect:/prir/files";
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot upload file. Unsupported extension!" );
            return "redirect:/prir/files";
        }
    }

    @PostMapping("/run")
    public String runAlgorithm(@ModelAttribute("runParams") RunParams runParams,
                               RedirectAttributes redirectAttributes) {

        CommandExecutor executor = new CommandExecutor();
        List<String> command = Stream.of(
                "python",
                "kmp.py",
                storageService.load(runParams.getSelectedFilename()).toString(),
                runParams.getSearchedText()
        ).collect(Collectors.toList());

        executor.executeCommand(command);

        redirectAttributes.addFlashAttribute("info",
                "Your result: " + executor.getResult() + " Positions: " + executor.getPositions());

        return "redirect:/prir/files";
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> fileNotFoundException(FileNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

}
