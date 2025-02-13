package com.DsCommerce.controlls.handlers;

import com.DsCommerce.dto.CustomError;
import com.DsCommerce.exceptions.DatabaseException;
import com.DsCommerce.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso não encontrado");
    }

    @ExceptionHandler(InvalidDefinitionException.class)
    public ResponseEntity<String> handleInvalidDefinition(InvalidDefinitionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro de definição inválida: " + e.getMessage());
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<String> handleDatabaseException(DatabaseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro no banco de dados: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class) // Captura outros erros genéricos
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor: " + e.getMessage());
    }
}
