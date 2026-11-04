package org.example.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyGroup {
    private Integer id;
    private String name;
    private Integer facultyId;
    private Integer admissionYear;
}
