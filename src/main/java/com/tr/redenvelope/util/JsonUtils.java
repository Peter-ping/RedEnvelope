package com.tr.redenvelope.util;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.SerializationConfig;
//import org.codehaus.jackson.map.SerializationConfig.Feature;
//import org.codehaus.jackson.type.TypeReference;


public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static ObjectMapper ignoreAnnotationObjectMapper = new ObjectMapper();

    static {

        //SerializationConfig serializationConfig = ignoreAnnotationObjectMapper.copySerializationConfig();
        //serializationConfig.set(Feature.USE_ANNOTATIONS, false);
        //	ignoreAnnotationObjectMapper.setSerializationConfig(serializationConfig);
    }

    public static <T> String toJsonForStorage(T t) {

        try {
            String jsonStr = ignoreAnnotationObjectMapper.writeValueAsString(t);
            return jsonStr;
        } catch (JsonGenerationException e) {
            throw new RuntimeException("JsonGenerationException", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("JsonMappingException", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
    }

    public static <T> String toJsonForPrettyPrint(T t) {

        try {
            String jsonStr = ignoreAnnotationObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
            return jsonStr;
        } catch (JsonGenerationException e) {
            throw new RuntimeException("JsonGenerationException", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("JsonMappingException", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
    }

    public static <T> String toJsonForDisplay(T t) {
        try {
            String jsonStr = objectMapper.writeValueAsString(t);
            return jsonStr;
        } catch (JsonGenerationException e) {
            throw new RuntimeException("JsonGenerationException", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("JsonMappingException", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
    }

    public static <T> T fromJSON(String jsonString, Class<T> clazz) {
        T object = null;
        try {
            if (jsonString != null) {
                object = objectMapper.readValue(jsonString, clazz);
            }
        } catch (JsonGenerationException e) {
            throw new RuntimeException("JsonGenerationException", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("JsonMappingException", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        }
        return object;
    }
//		public static <T> T fromJSON(String jsonString, TypeReference<T> typeReference) {
//
//			T object = null;
//			try {
//				if(jsonString!=null)
//				{
//				object = objectMapper.readValue(jsonString, typeReference);
//				}
//			} catch (JsonGenerationException e) {
//				throw new RuntimeException("JsonGenerationException", e);
//			} catch (JsonMappingException e) {
//				throw new RuntimeException("JsonMappingException", e);
//			} catch (IOException e) {
//				throw new RuntimeException("IOException", e);
//			}
//			return object;
//		}


}
