package com.practice.spring.boot.exception;

import org.springframework.dao.DataIntegrityViolationException;

import com.practice.spring.boot.constants.ApplicationConstants;
import com.practice.spring.boot.constants.ResponseStatusCode;
import com.practice.spring.boot.utils.ResponseObject;

public class ExceptionHandler {

	public static void setMessageCode(Exception e, ResponseObject response) {
		
        if (e == null) {
            response.setCode(ResponseStatusCode.ST_400);
            response.setMessage(ApplicationConstants.COMMON_EXCEPTION);
        } else if (e instanceof NullPointerException) {
            response.setCode(ResponseStatusCode.ST_204);
            response.setMessage(ApplicationConstants.NO_CONTENT);
        } else if (e instanceof FacadeException || e instanceof ServiceException) {

            response.setMessage(e.getMessage());

            if (e.getMessage().startsWith("INVALID_"))
                response.setCode(ResponseStatusCode.ST_404);
            else if(e.getMessage().contains("Data truncation") || e.getMessage().contains("Data too long")){
                response.setCode(ResponseStatusCode.ST_400);
                response.setMessage(ApplicationConstants.QUERY_EXCEPTION);

                if(e.getMessage().contains("Data too long")){
                    response.setMessage(ApplicationConstants.LENGTH_EXCEEDED);
                }
            } else if (e.getMessage().contains("Incorrect string value")){
                response.setCode(ResponseStatusCode.ST_501);
                response.setMessage(ApplicationConstants.CHARACTER_EXCEPTION);
            } else{
                response.setCode(ResponseStatusCode.ST_400);
            }

            if(!response.getMessage().contains("_")){
                response.setMessage(ApplicationConstants.COMMON_EXCEPTION);
            }

        } else if (e instanceof ValidationException) {
            response.setCode(ResponseStatusCode.ST_412);
            response.setMessage(e.getMessage());
        } else if(e instanceof NumberFormatException) {
            response.setCode(ResponseStatusCode.ST_500);
            response.setMessage(ApplicationConstants.COMMON_EXCEPTION);
        } else if(e instanceof DataIntegrityViolationException){
            response.setCode(ResponseStatusCode.ST_400);
            response.setMessage(ApplicationConstants.QUERY_EXCEPTION);

            if(e.getCause().getCause().getMessage().contains("Data too long")){
                response.setMessage(ApplicationConstants.LENGTH_EXCEEDED);
            }
        } else {
            response.setCode(ResponseStatusCode.ST_500);
            response.setMessage(ApplicationConstants.COMMON_EXCEPTION);
        }
    }
}
