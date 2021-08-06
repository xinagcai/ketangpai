package com.cqut.ktp.controller;

import com.cqut.ktp.pojo.Course;
import com.cqut.ktp.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@RequestMapping("/course")
@RestController
public class CourseController {
    @Autowired
    private CourseService service;

    @PostMapping(value = "/addCourse")
    public Boolean addCourse(@RequestBody Course course) {
        return service.addCourse(course);
    }


    @PutMapping(value = "/updateCourse")
    public Boolean updateCourse(@RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "year", required = false) String year,
                                @RequestParam(value = "semester", required = false) String semester,
                                @RequestParam(value = "num", required = false) Integer num,
                                @RequestParam(value = "archive", required = false) Integer archive,
                                @RequestParam(value = "deleteStatus", required = false) Integer deleteStatus,
                                @RequestParam String cno) {
        return service.updateCourse(name, year, semester, num, archive, deleteStatus, cno);
    }

    @GetMapping(value = "/getCourses")
    public List<Course> getCourses(@RequestParam String uid) {
        return service.getCourses(uid);
    }

    @GetMapping(value = "/getMembers")
    public Integer getMembers(@RequestParam String cno) {
        return service.getMembers(cno);
    }

    @GetMapping(value = "/getCourseByCno")
    public Course getCourseByCno(@RequestParam String cno) {
        return service.getCourseByCno(cno);
    }
}
