package com.vinod.saga.choreography.customer.util;

import org.springframework.http.ResponseEntity;

import java.util.Date;

public class GlobalUtility {

    /**
     * Response for success.
     *
     * @param statusCode
     * @param message
     * @param responseData
     * @return
     */
    public static ResponseEntity<Response> buildResponseForSuccess(final int statusCode, final String message, final Object responseData) {
        Response apiResponse = Response.builder().status(statusCode).message(message).data(responseData).build();
        return ResponseEntity.ok().body(apiResponse);
    }

    /**
     * Response for Failed.
     *
     * @param statusCode
     * @param errorCode
     * @param errorMessage
     * @param errorData
     * @return
     */
    public static ResponseEntity<Response> buildResponseForError(final int statusCode, final String errorCode,final String errorMessage, final Object errorData) {
        Response apiResponse = Response.builder().status(statusCode).errorCode(errorCode).errorMessage(errorMessage).errorData(errorData).build();
        return ResponseEntity.status(statusCode).body(apiResponse);
    }
}
