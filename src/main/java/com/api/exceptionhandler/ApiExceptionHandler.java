package com.api.exceptionhandler;

import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiError> tratarNaoEncontrado(EmptyResultDataAccessException exception) {
        return resposta(HttpStatus.NOT_FOUND,
                messageSource.getMessage("recurso.nao-encontrado", null, Locale.getDefault()),
                List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> tratarValidacao(MethodArgumentNotValidException exception) {
        List<String> erros = exception.getBindingResult().getFieldErrors().stream()
                .map(this::mensagemCampo)
                .toList();
        return resposta(HttpStatus.BAD_REQUEST, "Um ou mais campos são inválidos.", erros);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> tratarArgumentoInvalido(IllegalArgumentException exception) {
        return resposta(HttpStatus.BAD_REQUEST, exception.getMessage(), List.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> tratarParametroInvalido(MethodArgumentTypeMismatchException exception) {
        return resposta(HttpStatus.BAD_REQUEST, "Parâmetro inválido: " + exception.getName(), List.of());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> tratarConflito(IllegalStateException exception) {
        return resposta(HttpStatus.CONFLICT, exception.getMessage(), List.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> tratarIntegridade(DataIntegrityViolationException exception) {
        return resposta(HttpStatus.CONFLICT,
                "A operação conflita com dados existentes ou com um relacionamento obrigatório.", List.of());
    }

    private String mensagemCampo(FieldError erro) {
        return erro.getField() + ": " + erro.getDefaultMessage();
    }

    private ResponseEntity<ApiError> resposta(HttpStatus status, String mensagem, List<String> erros) {
        return ResponseEntity.status(status).body(
                new ApiError(LocalDateTime.now(), status.value(), mensagem, erros));
    }
}
