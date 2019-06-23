package com.lisak.services.accept;

import lombok.Data;

@Data
class ErrorDetail {
    private String attribute;
    private String message;
}