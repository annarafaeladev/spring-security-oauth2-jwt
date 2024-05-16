package com.auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ObjectMapperUtil {

    private static final ObjectMapper mapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .registerModule(new JavaTimeModule());

    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper().
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .registerModule(new JavaTimeModule());
    }

    public static Object safeWriteValueAsString(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            log.debug("safeWriteValueAsString() - [ERROR] - message <{}> - e: ", e.getMessage(), e);
            return obj;
        }
    }

    public static <T> String writeObjectAsJson(T object) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(object);
            log.debug("JSON = {}", json);

            return json;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T safeWriteValueAsObject(Object obj, Class<T> toValueType){
        try {
            return mapper.convertValue(obj,toValueType);
        }catch (Exception e){
            return null;
        }
    }
}
