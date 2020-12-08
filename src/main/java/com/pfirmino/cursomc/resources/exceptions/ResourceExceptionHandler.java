package com.pfirmino.cursomc.resources.exceptions;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.pfirmino.cursomc.services.exceptions.AuthorizationException;
import com.pfirmino.cursomc.services.exceptions.DataIntegrityException;
import com.pfirmino.cursomc.services.exceptions.ObjectNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest req) {
        StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }
    
    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest req) {
        StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validadtion(MethodArgumentNotValidException e, HttpServletRequest req) {
        ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de validação", new Date());
        for (FieldError x : e.getBindingResult().getFieldErrors()){
            err.addError(x.getField(), x.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandardError> AuthorizationException(AuthorizationException e, HttpServletRequest req) {
        StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(), e.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }
}
