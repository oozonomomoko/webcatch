package nogi.web.webcatch.controller;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.controller.dto.*;
import nogi.web.webcatch.util.ConfigUtil;
import nogi.web.webcatch.util.tail.LogListener;
import nogi.web.webcatch.service.CatchStarter;
import nogi.web.webcatch.service.Downloader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("main")
@ResponseBody
@Slf4j
public class MainController {
    @Resource
    private CatchStarter catchStarter;

    @Resource
    private Downloader downloader;

    /**
     * 开始
     *
     * @param request
     * @return
     */
    @RequestMapping("/start.do")
    public BaseResponse start(@RequestBody StartRequest request) {
        log.info("开始爬取, request:{}", JSON.toJSONString(request));
        if (!catchStarter.initThread()) {
            return new BaseResponse(false, "爬取任务正在执行，可点击结束停止");
        }
        if (!downloader.initThread()) {
            return new BaseResponse(false, "下载任务正在执行，可点击结束停止");
        }

        Downloader.headers.clear();
        catchStarter.submit(request.getSteps(), 0, request.getContents(), new HashMap<>());
        return new BaseResponse();
    }

    /**
     * 开始
     *
     * @return
     */
    @RequestMapping("/stop.do")
    public BaseResponse stop() {
        log.info("尝试停止任务...");
        boolean catchStop = catchStarter.stopThread();
        boolean downStop = downloader.stopThread();
        if (catchStop && downStop) {
            return new BaseResponse();
        }

        return new BaseResponse(false, "正在停止...");
    }

    /**
     * 请求日志
     *
     * @return
     */
    @RequestMapping("/log.do")
    public LogResponse log() {
        LogResponse logResponse = new LogResponse();
        logResponse.setLogs(LogListener.getLog());
        return logResponse;
    }

    /**
     * 查询配置
     *
     * @return
     */
    @RequestMapping("/querySetting.do")
    public QuerySettingResponse querySetting() {
        Map<String, String> allConfig = ConfigUtil.getAllConfig();
        QuerySettingResponse res = new QuerySettingResponse();
        res.setConfigs(allConfig);
        return res;
    }

    /**
     * 推送配置
     *
     * @return
     */
    @RequestMapping("/pushSetting.do")
    public BaseResponse pushSetting(@RequestBody PushSettingRequest request) {
        Map<String, String> pushConfigs = request.getConfigs();
        ConfigUtil.setConfig(pushConfigs);
        return new BaseResponse();
    }
}
