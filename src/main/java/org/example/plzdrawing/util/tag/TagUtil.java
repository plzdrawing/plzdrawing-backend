package org.example.plzdrawing.util.tag;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TagUtil {
    public static Map<String, String> normalize(List<String> raw) {
        if (raw == null) return Collections.emptyMap();
        Map<String, String> map = new LinkedHashMap<>();
        for (String s : raw) {
            if (s == null) continue;
            String trimmed = s.trim();
            if (trimmed.isEmpty()) continue;
            String key = trimmed.toLowerCase(Locale.ROOT);
            map.putIfAbsent(key, trimmed); // 첫 표기 유지
        }
        return map;
    }
}
