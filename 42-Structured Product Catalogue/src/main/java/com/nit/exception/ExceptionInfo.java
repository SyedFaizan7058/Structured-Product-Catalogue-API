package com.nit.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionInfo {
    private LocalDateTime timestamp;
    private String status;
    private String message;
}
