package nogi.web.webcatch.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class ConfigUtil {
    private static Properties properties = new Properties();

    static  {
        refresh();
    }

    private static void refresh() {
        try (InputStream defaultIs = ConfigUtil.class.getResourceAsStream("/default.properties");
             InputStream configIs = ConfigUtil.class.getResourceAsStream("/config.properties")) {
            properties.load(defaultIs);
            if (configIs == null) {
                return;
            }
            Properties propertiesSelf = new Properties();
            propertiesSelf.load(configIs);
            properties.putAll(propertiesSelf);
        } catch (IOException e) {
            log.error("read config error.", e);
        }
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
        properties.putAll(configs);
        try (OutputStream out = new FileOutputStream("config.properties")) {
            properties.store(out, null);
            refresh();
        } catch (IOException e) {
            log.error("save config error.", e);
        }
    }
}
