package com.example.tasksorter.service.impl;

import com.example.tasksorter.bean.TaskBean;
import com.example.tasksorter.bean.TasksBean;
import com.example.tasksorter.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {

    public List<TaskBean> sortTasks(TasksBean tasks) {
        Map<String, TaskBean> tasksMap = new HashMap<>();
        Map<String, Integer> reqCountMap = new HashMap<>();
        Map<String, List<String>> reqMap = new HashMap<>();
        Queue<TaskBean> taskQueue = new LinkedList<>();
        List<TaskBean> sortedList = new ArrayList<>();
        List<TaskBean> taskList = tasks.getTasks();

        for (TaskBean task : taskList) {
            tasksMap.put(task.getName(), task);
            reqCountMap.put(task.getName(), 0);
        }

        for (String task : tasksMap.keySet()) {
            List<String> reqTasks = tasksMap.get(task).getRequires();
            reqMap.put(task, reqTasks);
            reqCountMap.put(task, reqTasks.size());
        }

        for (String task : reqCountMap.keySet()) {
            if (reqCountMap.get(task) == 0) {
                taskQueue.offer(tasksMap.get(task));
            }
        }

        while (!taskQueue.isEmpty()) {
            TaskBean task = taskQueue.poll();
            sortedList.add(task);
            for (String t : reqMap.keySet()) {
                if (reqMap.get(t).contains(task.getName())) {
                    reqMap.get(t).remove(task.getName());
                    reqCountMap.put(t, reqCountMap.get(t) - 1);
                }
                if (reqCountMap.get(t) == 0 && !sortedList.contains(tasksMap.get(t))) {
                    taskQueue.offer(tasksMap.get(t));
                }
            }


        }
        if (sortedList.size() != taskList.size()) {
            throw new RuntimeException("Task is stuck waiting.");
        }


        return sortedList;
    }

    @Override
    public String getCommandsString(List<TaskBean> taskList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#!/bin/bash").append(System.lineSeparator());
        for (TaskBean task : taskList) {
            stringBuilder.append(task.getCommand()).append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

}
