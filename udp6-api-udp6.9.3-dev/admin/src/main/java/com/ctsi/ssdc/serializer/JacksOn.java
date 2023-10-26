package com.ctsi.ssdc.serializer;

import com.ctsi.ssdc.exception.Exceptions;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SuppressWarnings("unchecked")
public class JacksOn {

    private static ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksOn.class);

    private static final String CONFIG_NAME = "com.fasterxml.jackson.databind.ObjectMapper";

    static {
        ObjectMapperFactory objectMapperFactory;
        if (System.getProperty(CONFIG_NAME) != null) {
            try {
                objectMapperFactory = (ObjectMapperFactory) Class.forName(System.getProperty(CONFIG_NAME)).newInstance();
            } catch (Exception e) {
                LOGGER.error("instance ObjectMapperFactory from property error", e);
                objectMapperFactory = defaultObjectMapperFactory();
            }
        } else {
            objectMapperFactory = defaultObjectMapperFactory();
        }
        objectMapper = objectMapperFactory.getObjectMapper();
    }

    private static ObjectMapperFactory defaultObjectMapperFactory() {
        return () -> {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return objectMapper;
        };
    }

    public static String writeAsString(Object source) {
        if (source == null) {
            return null;
        }
        if (source instanceof String) {
            return source.toString();
        }
        try {
            return objectMapper.writeValueAsString(source);
        } catch (Exception e) {
            throw Exceptions.sneakyThrow(e);
        }
    }

    public static byte[] writeAsBytes(Object source) {
        String value = writeAsString(source);
        if (value == null) {
            return null;
        }
        return value.getBytes(StandardCharsets.UTF_8);
    }

    public static <T> T read(String source, Class<T> clazz) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        if (clazz.equals(String.class)) {
            return (T) source;
        }
        try {
            return objectMapper.readValue(source, clazz);
        } catch (Exception e) {
            throw Exceptions.sneakyThrow(e);
        }
    }

    public static <T> T read(String source, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        if (typeReference.getType().equals(String.class)) {
            return (T) source;
        }
        try {
            return objectMapper.readValue(source, typeReference);
        } catch (Exception e) {
            throw Exceptions.sneakyThrow(e);
        }
    }

    public static <T> T read(InputStream in, Class<T> clazz) {
        try {
            return objectMapper.readValue(in, clazz);
        } catch (Exception e) {
            throw Exceptions.sneakyThrow(e);
        }
    }

    public static <T> T read(InputStream in, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(in, typeReference);
        } catch (Exception e) {
            throw Exceptions.sneakyThrow(e);
        }
    }

    public static <T> T read(byte[] source, Class<T> clazz) {
        return getClazzTargetFromByteSource(source, clazz);
    }

    private static <T> T getClazzTargetFromByteSource(byte[] source, Class<T> clazz) {
        try {
            if (source == null || source.length == 0) {
                return null;
            }
            return objectMapper.readValue(source, clazz);
        } catch (Exception e) {
            throw Exceptions.sneakyThrow(e);
        }
    }

    public static <T> T read(byte[] source, TypeReference<T> typeReference) {
        try {
            if (source != null && source.length != 0) {
                return objectMapper.readValue(source, typeReference);
            }
            return null;
        } catch (Exception e) {
            throw Exceptions.sneakyThrow(e);
        }
    }

    public static Map<String, Object> map(Object source) {
        return read(writeAsString(source), new TypeReference<Map<String, Object>>() {
        });
    }

    public static ObjectMapper extend() {
        return objectMapper;
    }

}
