package ru.dreremin.internetbank.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {

    public static String serializeToJson(Object object)
            throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public static <T> T deserializeFromJson(String json, Class<T> clazz)
            throws JsonProcessingException, JsonMappingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }
}
