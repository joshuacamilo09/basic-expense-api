package com.joshua.expense_api.exception;

import org.springframework.beans.MethodInvocationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

/*
    Exception -> capturada globalmente -> transformada em JSON -> devolvida com HTTP correto
*/

@RestControllerAdvice //interceta execuções globalmente na API, ou seja, qualquer erro passa por aqui.
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) //diz: "Quando esta exceção acontecer, usa esse method.
    public ResponseEntity<ErrorResponse> handleErrors(ResourceNotFoundException ex){

        ErrorResponse error = new ErrorResponse(
                HttpStatus.ALREADY_REPORTED.value(),
                "ERRO OCORRIDO",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Handler para tratar de validação
    @ExceptionHandler(MethodInvocationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                message
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //Handler genérico para tratar de erros inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        // Pega o primeiro elemento do stack trace (onde o erro nasceu)
        StackTraceElement s = ex.getStackTrace()[0];

        // Monta uma string formatada: "Erro na classe ClasseTal (linha 42): Mensagem da exceção"
        String detailedMessage = String.format("Erro em %s na linha %d: %s",
                s.getClassName(),
                s.getLineNumber(),
                ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                detailedMessage
        );

        // Dica: Logue o erro no console para você conseguir debugar
        ex.printStackTrace();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
