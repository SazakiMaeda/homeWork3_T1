package com.sazakimaeda.h2.handler.exception;

public class QueueIsFullException extends RuntimeException{
    public QueueIsFullException(){
        super("Queue is full");
    }
}
