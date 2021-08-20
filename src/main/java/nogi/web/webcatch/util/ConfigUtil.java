package nogi.web.webcatch.util;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.util.tail.LogListener;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ConfigUtil {
    private static Properties properties = new Properties();

    static  {
        refresh();
    }

    private static void refresh() {
        try (InputStream defaultIs = ConfigUtil.class.getResourceAsStream("/default.properties");
             InputStream configIs = new FileInputStream("config.properties")) {
            properties.load(defaultIs);
            if (configIs == null) {
                log.info("未读取到配置文件：config.properties，使用默认配置");
                return;
            }
            log.info("读取到配置文件：config.properties");
            Properties propertiesSelf = new Properties();
            propertiesSelf.load(configIs);
            properties.putAll(propertiesSelf);
        } catch (IOException e) {
            log.error("read config error.", e);
        }
    }
    protected static Map<String, String> toMap(Properties properties) {
        Map<String, String> result = new HashMap<>();
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String name = (String) propertyNames.nextElement();
            String value = LogListener.decode(properties.getProperty(name));
            result.put(name, value);
        }
        return result;
    }
    public static Map<String, String> getAllConfig() {
        return toMap(properties);
    }
    public static String getConfig(String key) {
        return properties.getProperty(key);
    }

    public static boolean getBoolean(String key) {
        return Boolean.valueOf(properties.getProperty(key));
    }

    public static Integer getInteger(String key) {
        String property = properties.getProperty(key);
        return Integer.parseInt(property);
    }
    public static void setConfig(String key, Object value) {
        properties.put(key, String.valueOf(value));
        try (OutputStream out = new FileOutputStream("config.properties")) {
            properties.store(out, null);
            refresh();
        } catch (IOException e) {
            log.error("save config error.", e);
        }
    }
    public static void setConfig(Map<String, String> configs) {
        Map<String, String> collect = configs.entrySet().stream().filter(entry -> properties.get(entry.getKey()) != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        properties.putAll(collect);
        try (OutputStream out = new FileOutputStream("config.properties")) {
            properties.store(out, null);
        } catch (IOException e) {
            log.error("save config error.", e);
        }
    }
}
