package nogi.web.webcatch.service;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.dto.Operate;
import nogi.web.webcatch.util.ConfigUtil;
import nogi.web.webcatch.util.RegUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author 左手掐腰
 * @since 2019/10/11 15:39
 */
@Slf4j
@Service
public final class Downloader extends ThreadManage {
    public static Map<String, String> headers = new HashMap<>();

    public Future<?> download(String url, String filePath) {
        String format = url.replace("\\", "");
        return WORKS.submit(() -> {
            File f = new File(filePath);
            if (f.exists() && !ConfigUtil.getBoolean("download.file.exist.cover")) {
                return;
            }
            log.info(Operate.OPERATE_DOWNLOAD.getOperateName() + format + "--->" + filePath);
            try (BufferedInputStream bis = getConnection(format).execute().bodyStream()) {
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                // 替换已存在的文件
                Files.copy(bis, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                log.error("download file exception.", e);
            }
        });
    }

    public Document getDoc(String url) {
        try {
            Connection.Response rsp = getConnection(url).execute();
            if (rsp.statusCode() == 302) {
                return getDoc(rsp.header("location"));
            }
            return rsp.parse();
        } catch (IOException e) {
            log.error("get document failed:{}.", e);
        }
        return null;
    }

    private Connection getConnection(String url) {
        Proxy proxy = getProxy();
        Integer maxSize = ConfigUtil.getInteger("download.file.maxsize") * 1024 * 1024;
        if (null == proxy) {
            return Jsoup.connect(url).ignoreContentType(true).headers(headers).maxBodySize(maxSize);
        } else {
            return Jsoup.connect(url).ignoreContentType(true).headers(headers).maxBodySize(maxSize).proxy(proxy);
        }
    }

    private Proxy getProxy() {
        String proxy = ConfigUtil.getConfig("proxy.host");
        if (proxy == null || "".equals(proxy)) {
            return null;
        }
        String[] sp = proxy.split(":");
        int port = Integer.parseInt(sp[1]);
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(sp[0], port));
    }

    @Override
    public String getThreadName() {
        return "download";
    }
}
