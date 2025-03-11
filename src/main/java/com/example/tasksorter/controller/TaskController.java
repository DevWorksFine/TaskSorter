package com.example.tasksorter.controller;

import com.example.tasksorter.bean.TaskBean;
import com.example.tasksorter.bean.TasksBean;
import com.example.tasksorter.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<byte[]> sortTasks(@RequestBody TasksBean tasks) {
        List<TaskBean> sortedTasks = taskService.sortTasks(tasks);
        byte[] scriptBytes = taskService.getCommandsString(sortedTasks).getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated_script.sh");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-sh");

        return new ResponseEntity<>(scriptBytes, headers, HttpStatus.OK);
    }

}


