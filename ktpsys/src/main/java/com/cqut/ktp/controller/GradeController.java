package com.cqut.ktp.controller;

import com.cqut.ktp.pojo.Grade;
import com.cqut.ktp.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 欧阳建荣
 */
@RestController
@RequestMapping("/grade")
public class GradeController {
    @Autowired
    private GradeService service;

    @PutMapping(value = "/revise")
    public Boolean revise(@RequestParam String uid, @RequestParam String tno,
                          @RequestParam Integer score) {
        return service.revise(uid, tno, score);
    }

    @PostMapping(value = "/reviseMany")
    public Boolean reviseMany(@RequestParam String tno, @RequestParam Integer score,
                              @RequestBody List<String> member) {
        return service.reviseMany(member, tno, score);
    }

    @PostMapping(value = "/reviseMulti")
    public Boolean reviseMulti(@RequestBody Map<String, Object> map) {
        return service.reviseMulti(map);
    }

    @PutMapping(value = "/submit")
    public Boolean submit(@RequestParam String uid, @RequestParam String tno,
                          @RequestParam String answer) {
        return service.submit(uid, tno, answer);
    }

    @GetMapping(value = "/getGradeList")
    public List<Grade> getGradeList(@RequestParam String tno) {
        return service.getGradeList(tno);
    }

    @GetMapping(value = "/getScoreList")
    public List<Grade> getScoreList(@RequestParam String uid, @RequestParam String cno) {
        return service.getScoreList(uid, cno);
    }

    @GetMapping(value = "/getGrade")
    public Grade getGrade(@RequestParam String uid, @RequestParam String tno) {
        return service.getGrade(uid, tno);
    }
}
