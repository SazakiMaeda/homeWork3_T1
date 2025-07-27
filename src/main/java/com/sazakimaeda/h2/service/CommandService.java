package com.sazakimaeda.h2.service;

import com.sazakimaeda.h2.model.Command;
import org.springframework.stereotype.Service;

@Service
public interface CommandService {

    void executeCommand(Command command);

}
