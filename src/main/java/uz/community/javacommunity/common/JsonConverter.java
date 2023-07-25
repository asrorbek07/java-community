package uz.community.javacommunity.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonConverter {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public <T> T convertFromString(String source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        return objectMapper.readValue(source, targetType);
    }

    @SneakyThrows
    public <T> T convertFromString(String source, TypeReference<T> valueTypeRef) {
        if (source == null) {
            return null;
        }
        return objectMapper.readValue(source, valueTypeRef);
    }

    @SneakyThrows
    public <T> String convertToString(T t) {
        if (t == null) {
            return null;
        }
        return objectMapper.writeValueAsString(t);
    }

    @SneakyThrows
    public <T> T convertFromMap(Map<String, Object> map, Class<T> targetType) {
        if (map == null) {
            return null;
        }
        return objectMapper.convertValue(map, targetType);
    }
}
