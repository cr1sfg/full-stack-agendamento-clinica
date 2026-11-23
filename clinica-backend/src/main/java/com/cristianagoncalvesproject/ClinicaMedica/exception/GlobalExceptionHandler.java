package com.cristianagoncalvesproject.ClinicaMedica.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Padroniza as respostas de erro da API no formato ProblemDetail (RFC 9457),
 * sempre com uma mensagem legível em {@code detail}. Assim o frontend consegue
 * exibir ao usuário o motivo exato da falha.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> erros = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erros.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, "Existem campos inválidos na requisição.");
        problem.setTitle("Dados inválidos");
        problem.setProperty("errors", erros);
        return ResponseEntity.status(status).body(problem);
    }

    /** Exceções de domínio anotadas com {@link ResponseStatus} (ex.: CPF já cadastrado). */
    @ExceptionHandler({CpfJaCadastradoException.class, PacienteNaoEncontradoException.class})
    public ProblemDetail handleDominio(RuntimeException ex) {
        ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
        HttpStatus status = responseStatus != null ? responseStatus.code() : HttpStatus.BAD_REQUEST;
        return ProblemDetail.forStatusAndDetail(status, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleIntegridade(DataIntegrityViolationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
            "A operação viola uma restrição de integridade dos dados (registro duplicado ou com vínculos).");
    }
}
