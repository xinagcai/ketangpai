package com.cqut.ktp.controller;

import com.cqut.ktp.pojo.Course;
import com.cqut.ktp.pojo.User;
import com.cqut.ktp.service.CourseMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 11629
 */
@RestController
@RequestMapping("/course")
public class CourseMemberController {
    @Autowired
    private CourseMemberService service;

    @PutMapping(value = "/addMember")
    public Boolean addMember(@RequestParam String cno, @RequestParam String uid) {
        return service.addMember(cno, uid);
    }

    @PutMapping(value = "/changeStatus")
    public Boolean changeStatus(@RequestParam String cno, @RequestParam String uid, @RequestParam String status) {
        return service.changeStatus(cno, uid, status);
    }

    @PostMapping(value = "/deleteMembers")
    public Boolean deleteMembers(@RequestParam String cno, @RequestBody List<User> members) {
        return service.deleteMembers(cno, members);
    }

    @PutMapping(value = "/changeAllStatus")
    public Boolean changeAllStatus(@RequestParam String cno, @RequestParam String status) {
        return service.changeAllStatus(cno, status);
    }

    @GetMapping(value = "/getCourseMembers")
    public List<User> getCourseMembers(@RequestParam String cno) {
        return service.getCourseMembers(cno);
    }

    @GetMapping(value = "/getCourse")
    public List<Course> getCourse(@RequestParam String uid) {
        return service.getCourse(uid);
    }

    @GetMapping(value = "/getMemberCount")
    public Integer getMemberCount(@RequestParam String cno) {
        return service.getMemberCount(cno);
    }
}
