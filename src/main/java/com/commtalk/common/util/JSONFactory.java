package com.commtalk.common.util;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONFactory {

    public static String getJSONStringFromList(List<?> list) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(list);

        return jsonStr;
    }

    public static String getJSONStringFromMap(Map<String, ?> map) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(map);

        return jsonStr;
    }

    public static String getJSONStringFromObject(Object obj) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(obj);

        return jsonStr;
    }

}
