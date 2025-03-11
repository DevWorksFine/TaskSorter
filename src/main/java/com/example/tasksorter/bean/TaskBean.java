package com.example.tasksorter.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
public class TaskBean {
    private String name;
    private String command;
    private ArrayList<String> requires;

    public TaskBean(String name, String command, ArrayList<String> requires) {
        this.name = name;
        this.command = command;
        this.requires = requires != null ? requires : new ArrayList<>();
    }
}
