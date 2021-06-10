package com.gis.omp.common.error;

import com.gis.omp.common.api.CommonResult;
import com.gis.omp.common.api.ResultCode;
import com.gis.omp.common.auth.PermissionDeniedException;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 *  global exception interceptor, when throw an exception
 */
@RestControllerAdvice
public class GlobalExceptionTranslator {

    static final ILogger logger = SLoggerFactory.getLogger(GlobalExceptionTranslator.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonResult handleError(MissingServletRequestParameterException e) {
        logger.warn("Missing request Parameter", e);
        String message = String.format("Missing request Parameter: %s", e.getParameterName());
        return CommonResult
                .builder()
                .code(ResultCode.PARAM_MISS.getCode())
                .message(message)
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonResult handleError(MethodArgumentTypeMismatchException e) {
        logger.warn("Method Argument Type Mismatch", e);
        String message = String.format("Method Argument Type Mismatch: %s", e.getName());
        return CommonResult
                .builder()
                .code(ResultCode.PARAM_TYPE_ERROR.getCode())
                .message(message)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult handleError(MethodArgumentNotValidException e) {
        logger.warn("Method Argument Not Valid", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return CommonResult
                .builder()
                .code(ResultCode.PARAM_VALID_ERROR.getCode())
                .message(message)
                .build();
    }

    @ExceptionHandler(BindException.class)
    public CommonResult handleError(BindException e) {
        logger.warn("Bind Exception", e);
        FieldError error = e.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return CommonResult
                .builder()
                .code(ResultCode.PARAM_BIND_ERROR.getCode())
                .message(message)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult handleError(ConstraintViolationException e) {
        logger.warn("Constraint Violation", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return CommonResult
                .builder()
                .code(ResultCode.PARAM_VALID_ERROR.getCode())
                .message(message)
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResult handleError(NoHandlerFoundException e) {
        logger.error("404 Not Found", e);
        return CommonResult
                .builder()
                .code(ResultCode.NOT_FOUND.getCode())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResult handleError(HttpMessageNotReadableException e) {
        logger.error("Message Not Readable", e);
        return CommonResult
                .builder()
                .code(ResultCode.MSG_NOT_READABLE.getCode())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult handleError(HttpRequestMethodNotSupportedException e) {
        logger.error("request Method Not Supported", e);
        return CommonResult
                .builder()
                .code(ResultCode.METHOD_NOT_SUPPORTED.getCode())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public CommonResult handleError(HttpMediaTypeNotSupportedException e) {
        logger.error("Media Type Not Supported", e);
        return CommonResult
                .builder()
                .code(ResultCode.MEDIA_TYPE_NOT_SUPPORTED.getCode())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(ServiceException.class)
    public CommonResult handleError(ServiceException e) {
        logger.error("Service Exception", e);
        return CommonResult
                .builder()
                .code(e.getResultCode().getCode())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public CommonResult handleError(PermissionDeniedException e) {
        logger.error("Permission Denied", e);
        return CommonResult
                .builder()
                .code(e.getResultCode().getCode())
                .message(e.getMessage())
                .build();
    }

    /*
    if not catch, base class Throwable, extend to error and exception
     */
    @ExceptionHandler(Throwable.class)
    public CommonResult handleError(Throwable e) {
        logger.error("Internal Server Error", e);
        return CommonResult
                .builder()
                .code(ResultCode.INTERNAL_SERVER_ERROR.getCode())
                .message(e.getMessage())
                .build();
    }
}
