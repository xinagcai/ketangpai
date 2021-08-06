package com.cqut.ktp.controller;

import com.cqut.ktp.pojo.School;
import com.cqut.ktp.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@RestController
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    @GetMapping(value = "/selectSchool")
    public List<School> selectSchool() {
        return schoolService.selectSchool();
    }
}
