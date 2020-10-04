package com.vinod.saga.choreography.customer.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.exception.ExceptionUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Log4j2
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

    /**
     * Convert object to json.
     *
     * @param input     - Input object.
     * @return          - JSON string object.
     */
    public static String convertObjectToJson(final Object input) {
        try {
            JavaTimeModule javaTimeModule=new JavaTimeModule();
            javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
            ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().modules(javaTimeModule).featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            log.error("Error generated while converting object to JSON error msg: {}", ExceptionUtils.getRootCauseMessage(e));
        }
        return null;
    }
}
