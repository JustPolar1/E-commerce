package com.store.digital.exception;

import com.store.digital.dto.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler({ConstraintViolationException.class, HandlerMethodValidationException.class})
	public ResponseEntity<ApiErrorResponse> handleInvalidRequest(Exception exception, HttpServletRequest request) {
		return error(HttpStatus.BAD_REQUEST, "El identificador del producto no tiene un formato válido.", request);
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleProductNotFound(
			ProductNotFoundException exception,
			HttpServletRequest request) {
		return error(HttpStatus.NOT_FOUND, "No se encontró el producto solicitado.", request);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ApiErrorResponse> handleUpstreamClientError(
			HttpClientErrorException exception,
			HttpServletRequest request) {
		return error(HttpStatus.BAD_GATEWAY, "Uno de los servicios requeridos rechazó la solicitud.", request);
	}

	@ExceptionHandler(ResourceAccessException.class)
	public ResponseEntity<ApiErrorResponse> handleServiceUnavailable(
			ResourceAccessException exception,
			HttpServletRequest request) {
		return error(HttpStatus.SERVICE_UNAVAILABLE, "Uno de los servicios requeridos no está disponible.", request);
	}

	@ExceptionHandler(HttpServerErrorException.class)
	public ResponseEntity<ApiErrorResponse> handleUpstreamServerError(
			HttpServerErrorException exception,
			HttpServletRequest request) {
		return error(HttpStatus.BAD_GATEWAY, "Uno de los servicios requeridos devolvió un error.", request);
	}

	@ExceptionHandler({RestClientException.class, UpstreamServiceException.class})
	public ResponseEntity<ApiErrorResponse> handleUpstreamError(Exception exception, HttpServletRequest request) {
		return error(HttpStatus.BAD_GATEWAY, "No fue posible obtener una respuesta válida de los servicios requeridos.", request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnexpectedError(Exception exception, HttpServletRequest request) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado al procesar la solicitud.", request);
	}

	private ResponseEntity<ApiErrorResponse> error(HttpStatus status, String message, HttpServletRequest request) {
		ApiErrorResponse response = new ApiErrorResponse(
				Instant.now(), status.value(), status.getReasonPhrase(), message, request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}
}
