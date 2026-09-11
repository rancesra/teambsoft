package co.edu.uis.catalogo.error;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ManejadorGlobalErrores {

	private static final String PRODUCTO_NO_ENCONTRADO = "PRODUCTO_NO_ENCONTRADO";
	private static final String VALIDACION_FALLIDA = "VALIDACION_FALLIDA";

	@ExceptionHandler(ProductoNoEncontradoException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse productoNoEncontrado(ProductoNoEncontradoException e) {
		return new ErrorResponse(PRODUCTO_NO_ENCONTRADO, e.getMessage());
	}

	@ExceptionHandler(ValidacionFallidaException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse validacionFallida(ValidacionFallidaException e) {
		return new ErrorResponse(VALIDACION_FALLIDA, e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse cuerpoInvalido(MethodArgumentNotValidException e) {
		String mensaje = e.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return new ErrorResponse(VALIDACION_FALLIDA, mensaje);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse parametrosInvalidos(HandlerMethodValidationException e) {
		String mensaje = e.getParameterValidationResults().stream()
				.flatMap(resultado -> resultado.getResolvableErrors().stream()
						.map(error -> resultado.getMethodParameter().getParameterName() + ": "
								+ error.getDefaultMessage()))
				.collect(Collectors.joining("; "));
		return new ErrorResponse(VALIDACION_FALLIDA, mensaje);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse tipoDeParametroInvalido(MethodArgumentTypeMismatchException e) {
		return new ErrorResponse(VALIDACION_FALLIDA, "El parámetro '" + e.getName() + "' tiene un valor inválido");
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse cuerpoIlegible(HttpMessageNotReadableException e) {
		return new ErrorResponse(VALIDACION_FALLIDA, "El cuerpo de la petición no es un JSON válido o tiene tipos incorrectos");
	}

}