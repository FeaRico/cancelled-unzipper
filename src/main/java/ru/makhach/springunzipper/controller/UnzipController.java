package ru.makhach.springunzipper.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.makhach.springunzipper.service.ConcurrentArchiveService;

@RestController
@RequestMapping("/api/v1/archive")
public class UnzipController {
    private final ConcurrentArchiveService concurrentArchiveService;

    public UnzipController(ConcurrentArchiveService concurrentArchiveService) {
        this.concurrentArchiveService = concurrentArchiveService;
    }

    @PostMapping("/start")
    public String start(@RequestBody String zipPath) {
        return concurrentArchiveService.process(zipPath);
    }

    @PostMapping("/cancel")
    public boolean cancel(@RequestBody String key) {
        return concurrentArchiveService.cancel(key);
    }
}
