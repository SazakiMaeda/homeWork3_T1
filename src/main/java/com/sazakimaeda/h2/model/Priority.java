package com.sazakimaeda.h2.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Priority {
    COMMON("COMMON"),
    CRITICAL("CRITICAL");

    private final String command;
}
