package com.example.tasksorter.service;

import com.example.tasksorter.bean.TaskBean;
import com.example.tasksorter.bean.TasksBean;

import java.util.List;

public interface TaskService {

  List<TaskBean> sortTasks(TasksBean tasks);

  String getCommandsString(List<TaskBean> taskList);
}
