package org.example.models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Subject {
    private Integer id;
    private String name;
    private Integer course;
    private Faculty faculty;
}
