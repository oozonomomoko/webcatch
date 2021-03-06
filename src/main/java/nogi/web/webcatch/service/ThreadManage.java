package nogi.web.webcatch.service;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.util.ConfigUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class ThreadManage {
    /**
     * 任务线程
     */
    public ExecutorService WORKS;

    public abstract String getThreadName();

    /**
     * 重新初始化线程
     */
    public boolean initThread() {
        String threadName = getThreadName();
        if (isRunning()) {
            log.info("{}线程未停止", threadName);
            return false;
        }
        Integer coreCount = ConfigUtil.getInteger("thread." + threadName + ".count");
        WORKS = Executors.newFixedThreadPool(coreCount);
        log.info("初始化{}线程成功，线程数：{}", threadName, coreCount);
        return true;
    }

    public boolean stopThread() {
        if (!isRunning()) {
            log.info("{}线程已停止", getThreadName());
            return true;
        }
        WORKS.shutdownNow();
        log.info("尝试停止{}线程...", getThreadName());
        return false;
    }

    public boolean isRunning() {
        return WORKS != null && !WORKS.isTerminated();
    }
}
