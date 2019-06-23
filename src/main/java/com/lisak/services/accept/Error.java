package com.lisak.services.accept;

import lombok.Data;

import java.util.List;

@Data
class Error {
    private String errorType;
    private String errorCode;
    private List<ErrorDetail> errors;
}