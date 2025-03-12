package com.example.tasksorter.service.impl;

import com.example.tasksorter.bean.TaskBean;
import com.example.tasksorter.bean.TasksBean;
import com.example.tasksorter.service.TaskService;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {

    public List<TaskBean> sortTasks(TasksBean tasks) {
        //HashMap for faster lookup.
        Map<String, TaskBean> tasksMap = new HashMap<>();
        //HashMap for keeping count of required tasks of a task.
        Map<String, Integer> reqCountMap = new HashMap<>();
        //HashMap for keeping the names of the required tasks.
        Map<String, List<String>> reqMap = new HashMap<>();
        //Queue to ensure FIFO.
        Queue<TaskBean> taskQueue = new LinkedList<>();
        //List to hold the sorted tasks.
        List<TaskBean> sortedList = new ArrayList<>();

        for (TaskBean task : tasks.getTasks()) {
            tasksMap.put(task.getName(), task);
            reqCountMap.put(task.getName(), 0);
        }

        for (String task : tasksMap.keySet()) {
            List<String> reqTasks = tasksMap.get(task).getRequires();
            reqMap.put(task, reqTasks);
            reqCountMap.put(task, reqTasks.size());
        }

        //If a task has 0 required tasks, should be directly put into the queue.
        for (String task : reqCountMap.keySet()) {
            if (reqCountMap.get(task) == 0) {
                taskQueue.offer(tasksMap.get(task));
            }
        }

        while (!taskQueue.isEmpty()) {
            TaskBean task = taskQueue.poll();
            sortedList.add(task);
            //For each task that has a dependency on the task that was sorted, remove the already sorted task from their dependency list.
            for (String t : reqMap.keySet()) {
                if (reqMap.get(t).contains(task.getName())) {
                    reqMap.get(t).remove(task.getName());
                    reqCountMap.put(t, reqCountMap.get(t) - 1);
                }
                //After processing the dependency list, if a task has no dependencies, it should go in the queue to be sorted.
                if (reqCountMap.get(t) == 0 && !sortedList.contains(tasksMap.get(t))) {
                    taskQueue.offer(tasksMap.get(t));
                }
            }


        }

        //Cycle detection
        if (sortedList.size() != tasks.getTasks().size()) {
            throw new RuntimeException("Task is stuck waiting.");
        }


        return sortedList;
    }

    @Override
    public String getCommandsString(List<TaskBean> taskList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#!/usr/bin/env bash").append(System.lineSeparator());
        for (TaskBean task : taskList) {
            stringBuilder.append(task.getCommand()).append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

}
