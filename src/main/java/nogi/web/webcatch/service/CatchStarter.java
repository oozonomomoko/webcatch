package nogi.web.webcatch.service;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.step.BaseStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 左手掐腰
 * @since 2019/10/11 11:11
 */
@Slf4j
@Service
public class CatchStarter extends ThreadManage {
    @Autowired
    private Map<String, BaseStep> stepMap;

    @Override
    public String getThreadName() {
        return "catch";
    }

    /**
     * @param steps
     * @param index
     * @param contents
     * @param variables
     */
    public void submit(List<Map<String, String>> steps, int index, List<String> contents, Map<String, String> variables) {
        if (steps.size() <= index || index < 0) {
            return;
        }
        Map<String, String> operateDetail = steps.get(index);
        if (CollectionUtils.isEmpty(contents)) {
            log.info("步骤：{}.{}，没有待处理内容", index, operateDetail.get("operate"));
            return;
        }
        BaseStep operate = stepMap.get(operateDetail.get("operate") + "Step");
        if (operate == null) {
            log.info("步骤：{}，未找到对应步骤的实现：{}", index, operateDetail.get("operate"));
        }
        WORKS.submit(() -> operate.process(steps, index, contents, new HashMap<>(variables)));
    }
}
