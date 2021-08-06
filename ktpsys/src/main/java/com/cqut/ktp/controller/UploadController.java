package com.cqut.ktp.controller;

import com.cqut.ktp.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.SocketException;

/**
 * @author 欧阳建荣
 */
@RestController
@RequestMapping("/file")
public class UploadController {
    @Autowired
    UploadService service;

    @PostMapping(value = "/upload")
    public String upload(@RequestParam MultipartFile file, @RequestParam String uid) throws SocketException, IOException {
        return service.upload(file, uid);
    }
}
