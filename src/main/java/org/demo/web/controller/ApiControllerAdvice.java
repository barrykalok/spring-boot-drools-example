package org.demo.web.controller;

import org.demo.DemoException;
import org.demo.web.model.BasicResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by barry.wong
 */
@ControllerAdvice
public class ApiControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ApiControllerAdvice.class);

    @Autowired
    @Qualifier("apiErrorMessageSource")
    private MessageSource apiErrorMessageSource;

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DemoException.class)
    BasicResponse handleJumpSeatError(DemoException e) {
        logger.trace("jumpseat error", e.toString());

        BasicResponse response = new BasicResponse();
        response.setErrorCode(e.getErrorCode());
        response.setErrorMessage(getLocalizedErrorMessage(e.getErrorCode()));
        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    BasicResponse processValidationError(HttpServletRequest httpRequest, MethodArgumentNotValidException ex) {
        logger.trace("validation error", ex);

        BindingResult result = ex.getBindingResult();
        BasicResponse response = new BasicResponse();
        resolve(response, result);
        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    BasicResponse httpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.trace("http request parse exception", e);

        BasicResponse response = new BasicResponse();
        response.setErrorCode("DEMO-ERR-201");
        response.setErrorMessage(getLocalizedErrorMessage("DEMO-ERR-201"));
        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    BasicResponse runtimeExceptionHandler(RuntimeException e) {
        logger.trace("internal server exception", e);

        BasicResponse response = new BasicResponse();
        response.setErrorCode("DEMO-ERR-100");
        response.setErrorMessage(getLocalizedErrorMessage("DEMO-ERR-100"));
        return response;
    }

    private void resolve(BasicResponse response, BindingResult result) {
        if (result.getGlobalErrorCount() > 0) {
            resolveError(response, result.getGlobalError());
        } else if (result.getFieldErrorCount() > 0) {
            resolveError(response, result.getFieldError());
        } else {
            response.setErrorCode("DEMO-ERR-200");
            response.setErrorMessage(getLocalizedErrorMessage("DEMO-ERR-200"));
        }
    }

    private void resolveError(BasicResponse response, ObjectError error) {
        Locale currentLocale = LocaleContextHolder.getLocale();

        String defaultMessage = error.getDefaultMessage();
        if (StringUtils.isNotBlank(defaultMessage) && defaultMessage.startsWith("{DEMO-")) {
            String errorCode = defaultMessage.substring(1, defaultMessage.length() - 1);
            response.setErrorCode(errorCode);
            response.setErrorMessage(getLocalizedErrorMessage(errorCode, error.getArguments()));
        } else {
            String[] errorCodes = error.getCodes();
            for (String code : errorCodes) {
                String message = getLocalizedErrorMessage(currentLocale, code, error.getArguments());
                if (!code.equals(message)) {
                    response.setErrorCode(code);
                    response.setErrorMessage(message);
                    return;
                }
            }

            response.setErrorCode(error.getCode());
            response.setErrorMessage(getLocalizedErrorMessage(error.getCode(), error.getArguments()));
        }
    }

    private String getLocalizedErrorMessage(String code, Object... arguments) {
        return getLocalizedErrorMessage(LocaleContextHolder.getLocale(), code, arguments);
    }

    private String getLocalizedErrorMessage(Locale currentLocale, String code, Object... arguments) {
        return apiErrorMessageSource.getMessage(code, arguments, currentLocale);
    }
}
