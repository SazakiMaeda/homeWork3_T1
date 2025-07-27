package com.sazakimaeda.h21.contoller;

import com.sazakimaeda.h2.annotation.WeylandWatchingYou;
import com.sazakimaeda.h2.model.Command;
import com.sazakimaeda.h2.service.CommandService;
import com.sazakimaeda.h21.dto.CommandDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("/api/v1/command")
@RequiredArgsConstructor
public class CommandController {


    private CommandService commandService;

    @PostMapping
    public ResponseEntity<Void> submitCommand(@Valid
                                              @RequestBody Command command) {
        commandService.executeCommand(command);
        return ResponseEntity.accepted().build();
    }
}
