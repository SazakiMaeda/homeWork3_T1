package com.sazakimaeda.h2.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class Command {

    @NotBlank(message = "Описание исполняемой команды не может быть пустым")
    @Size(max = 1000, message = "Описание команды не может превышать 1000 символов")
    private String description;

    @NotNull(message = "Приоритет выполнения команды не может быть пустым")
    private Priority priority;

    @Size(max = 100, message = "Автор не может быть длиннее 100 символов")
    @NotNull(message = "Автор команды не может быть ноу неймом")
    private String author;

    @NotNull(message = "Нет времени объяснять, время нужно выставить обязательно!")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
    private LocalDateTime date;
}
