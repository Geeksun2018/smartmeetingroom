package cn.hephaestus.smartmeetingroom.controller.exception;

import cn.hephaestus.smartmeetingroom.common.RetJson;
import cn.hephaestus.smartmeetingroom.utils.LogUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public RetJson resolveMethodArgumentNotValidException(ConstraintViolationException ex){
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        StringBuilder msgBuilder = new StringBuilder();
        for(ConstraintViolation constraintViolation :constraintViolations){
            msgBuilder.append(constraintViolation.getMessage()).append(",");
        }
        String errorMessage = msgBuilder.toString();
        if(errorMessage.length()>1){
            errorMessage = errorMessage.substring(0,errorMessage.length()-1);
        }
        LogUtils.getExceptionLogger().error(errorMessage);
        return RetJson.fail(-1,errorMessage);
    }
}
