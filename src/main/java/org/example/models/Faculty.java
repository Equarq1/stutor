package org.example.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Faculty {
    private Integer id;
    private String name;
}
