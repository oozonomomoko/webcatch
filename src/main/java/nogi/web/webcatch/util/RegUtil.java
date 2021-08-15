package nogi.web.webcatch.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author 左手掐腰
 * @since 2019/10/12 17:49
 */
@Slf4j
public class RegUtil {
    public static final int FIND_REG = 1;
    public static final int FIND_CSS = 2;
    public static final int FIND_JSON = 3;
    public static final int FIND_XML = 4;
    public static final int FIND_VAR = 5;

    public static final int CSSTYPE_ATTR = 1;
    public static final int CSSTYPE_OUTER = 2;
    public static final int CSSTYPE_INNER = 3;


    /**
     * 正则表达式缓存
     */
    private static Map<String, Pattern> regTemp = new ConcurrentHashMap<>();

    /**
     * windows文件命名不可用字符
     * \ / :  * ? " < > |
     */
    private static final Pattern illegal = Pattern.compile("[\\\\\\n\\r/\\*<>|]|((?<!^\\w):)");

    public static String getLegalName(String fileName) {
        String[] split = fileName.split("\\\\");
        List<String> result = new ArrayList<>();
        for (String sp : split) {
            result.add(illegal.matcher(sp).replaceAll(""));
        }
        return StringUtil.join(result, "\\");
    }

    /**
     * 查找字符串
     */

    public static List<String> find(String content, int findType, String express, int cssType, String attrName) {
        List<String> result = new ArrayList<>();
        try {
            if (FIND_VAR == findType) {
                result.add(express);
                return result;
            }
            switch (findType) {
                case FIND_REG:
                    return findByReg(content, express);
                case FIND_CSS:
                    return findByCss(content, express, cssType, attrName);
                case FIND_JSON:
                    return findByJpath(content, express);
//                case FIND_XML:
//                    return findByXpath(content, express);
            }
        } catch (Exception e) {
            log.error("find faild, findType:{}, express:{}", findType, express);
        }
        return result;
    }

    private static List<String> findByJpath(String content, String express) {
        String text = Jsoup.parse(content).body().text();
        Object eval = JSONPath.eval(JSON.parseObject(text), express);
        if (eval == null) {
            return null;
        }
        if (eval instanceof Collection) {
            Collection<Object> list = (Collection<Object>) eval;
            return list.stream().map(String::valueOf).collect(Collectors.toList());
        }
        return Collections.singletonList(String.valueOf(eval));
    }

    private static List<String> findByCss(String content, String express, int cssType, String attrName) {
        Elements select = Jsoup.parse(content).select(express);
        List<String> result = new ArrayList<>();
        if (CSSTYPE_ATTR == cssType) {
            select.forEach(element -> result.add(element.attr(attrName)));
        } else if (CSSTYPE_OUTER == cssType) {
            select.forEach(element -> result.add(element.toString()));
        } else if (CSSTYPE_INNER == cssType) {
            select.forEach(element -> result.add(element.html()));
        }
        return result;
    }

    private static List<String> findByReg(String content, String express) {
        Pattern p = regTemp.get(express);
        if (null == p) {
            p = Pattern.compile(express);
            regTemp.putIfAbsent(express, p);
        }

        Matcher m = p.matcher(content);

        List<String> result = new ArrayList<>();
        while (m.find()) {
            result.add(m.group());
        }
        return result;
    }
}
