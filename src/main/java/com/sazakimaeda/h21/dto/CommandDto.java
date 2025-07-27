package com.sazakimaeda.h21.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommandDto {
    private String description;
    private Priority priority;
    private String author;
    private LocalDateTime date;

    enum Priority {
        COMMON,
        CRITICAL;
    }
}
