package nogi.web.webcatch.util;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public final class VarUtil {
    public static String replaceVar(String src, Map<String, String> variables) {
        Set<Map.Entry<String, String>> entrys = variables.entrySet();
        for (Map.Entry<String, String> entry : entrys) {
            src = src.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return src;
    }
}
