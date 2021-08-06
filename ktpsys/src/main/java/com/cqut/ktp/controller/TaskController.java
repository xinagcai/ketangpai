package com.cqut.ktp.controller;

import com.cqut.ktp.pojo.Task;
import com.cqut.ktp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 陈佳铭
 */
@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping(value = "/taskList")
    public List<Task> selectUsers(@RequestParam(value = "tno", required = false) String tno, @RequestParam String cno) {
        return taskService.selectTasks(tno, cno);
    }

    @PostMapping(value = "/insertTask")
    public void insertTask(@RequestBody Task task) {
        taskService.insertTask(task);
    }

    @PostMapping(value = "/updateTask")
    public void updateTask(@RequestBody Task task) {
        taskService.updateTask(task);
    }

    @PostMapping(value = "/deleteTask")
    public void updateTask(@RequestParam String tno) {
        taskService.deleteTask(tno);
    }
}
