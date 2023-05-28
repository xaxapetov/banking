package ru.iql.banking.dtos.errors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String reasonPhrase;

    private int status;

    private String errorMessage;
}
