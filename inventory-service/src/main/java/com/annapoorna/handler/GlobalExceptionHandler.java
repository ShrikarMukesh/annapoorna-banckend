package com.annapoorna.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.annapoorna.dto.ErrorResponse;
import com.annapoorna.exception.BadRequestException;
import com.annapoorna.exception.InsufficientInventoryException;
import com.annapoorna.exception.ResourceNotFoundException;
import com.annapoorna.service.AuditLogService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final AuditLogService auditLogService;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        log.error("Resource not found: {}", ex.getMessage());

        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());

        auditLogService.logError(
                "RESOURCE_NOT_FOUND",
                extractEntityType(request.getRequestURI()),
                null,
                null,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null,
                getClientIp(request));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientInventoryException(
            InsufficientInventoryException ex, HttpServletRequest request) {

        log.error("Insufficient inventory: {}", ex.getMessage());

        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());

        auditLogService.logError(
                "INSUFFICIENT_INVENTORY",
                "InventoryItem",
                null,
                null,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null,
                getClientIp(request));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex, HttpServletRequest request) {

        log.error("Bad request: {}", ex.getMessage());

        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());

        auditLogService.logError(
                "BAD_REQUEST",
                extractEntityType(request.getRequestURI()),
                null,
                null,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null,
                getClientIp(request));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {

        log.error("Illegal argument: {}", ex.getMessage());

        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());

        auditLogService.logError(
                "ILLEGAL_ARGUMENT",
                extractEntityType(request.getRequestURI()),
                null,
                null,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null,
                getClientIp(request));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("Validation failed: {}", errors);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("errors", errors);
        response.put("path", request.getRequestURI());

        auditLogService.logError(
                "VALIDATION_ERROR",
                extractEntityType(request.getRequestURI()),
                null,
                null,
                HttpStatus.BAD_REQUEST.value(),
                errors.toString(),
                null,
                getClientIp(request));

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {

        log.error("Unexpected error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI());

        auditLogService.logError(
                "UNEXPECTED_ERROR",
                extractEntityType(request.getRequestURI()),
                null,
                null,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                null,
                getClientIp(request));

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .traceId(UUID.randomUUID().toString())
                .build();
    }

    private String extractEntityType(String uri) {
        if (uri == null || uri.isEmpty()) {
            return "Unknown";
        }
        String[] parts = uri.split("/");
        for (String part : parts) {
            if (!part.isEmpty() && !part.equals("api")) {
                return part.substring(0, 1).toUpperCase() + part.substring(1);
            }
        }
        return "Unknown";
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
