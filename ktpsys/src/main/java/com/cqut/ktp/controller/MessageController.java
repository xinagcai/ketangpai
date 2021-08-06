package com.cqut.ktp.controller;

import com.cqut.ktp.pojo.Message;
import com.cqut.ktp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService service;

    @GetMapping(value = "/getNotReadCount")
    public Integer getNotReadMsgCount(@RequestParam String to) {
        return service.getNotReadMsgCount(to);
    }

    @GetMapping(value = "/getMessages")
    public List<Message> getMessages(@RequestParam String to, @RequestParam(value = "status", required = false) Integer status) {
        return service.getMessages(to, status);
    }

    @PostMapping(value = "/send")
    public Boolean send(@RequestBody Message message) {
        return service.send(message);
    }

    @PostMapping(value = "/read")
    public Boolean read(@RequestBody List<String> msgIds) {
        return service.read(msgIds);
    }
}
