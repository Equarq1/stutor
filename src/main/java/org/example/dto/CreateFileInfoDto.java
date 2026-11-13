package org.example.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateFileInfoDto {
    private String originalFileName;
    private Long size;
    private String type;
}
