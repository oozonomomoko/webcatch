package nogi.web.webcatch.util.tail;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class LogListener {
    private static List<String> logTemp = Collections.synchronizedList(new ArrayList<>());

    private static ExecutorService singleThread = Executors.newSingleThreadExecutor();

    static {
        singleThread.submit(() -> monitor("logs/spring.log", 5000));
    }

    /**
     * @param inputFile     监控文件
     * @param sleepInterval 当文件没有日志时sleep间隔
     */
    private static void monitor(String inputFile, int sleepInterval) {
        logTemp.clear();
        TailerListener listener = new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
                if (line == null || line.equals("")) {
                    log.warn("should not read empty line.");
                    return;
                }
                if (logTemp.size() > 10000) {
                    logTemp.clear();
                }
                logTemp.add(decode(line));
            }
        };
        Tailer tailer = new Tailer(new File(inputFile), listener, sleepInterval, true);
        tailer.run();
    }

    public static String decode(String line) {
        char[] chs = line.toCharArray();
        byte[] be = new byte[chs.length];
        for (int i = 0; i < chs.length; i++) {
            be[i] = (byte) chs[i];
        }
        return new String(be);
    }

    public static List<String> getLog() {
        List<String> logs = new ArrayList<>(logTemp);
        logTemp.clear();
        return logs;
    }
}
