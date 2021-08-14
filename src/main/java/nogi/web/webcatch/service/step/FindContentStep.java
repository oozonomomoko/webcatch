package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.dto.Operate;
import nogi.web.webcatch.service.CatchStarter;
import nogi.web.webcatch.service.Downloader;
import nogi.web.webcatch.util.RegUtil;
import nogi.web.webcatch.util.VarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class FindContentStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;

    @Override
    public void process(List<Map<String, String>> steps, int index, List<String> contents, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        // 1.正则表达式 2.CSS选择器 3.JPath 4.XPath 5.设置变量
        int findType = Integer.parseInt(operateDetail.get("findType"));
        String express = VarUtil.replaceVar(operateDetail.get("express"), variables);
        int cssType = Integer.parseInt(operateDetail.get("cssType"));
        String attrName = VarUtil.replaceVar(operateDetail.get("attrName"), variables);

        contents.forEach(content -> {
            List<String> result = RegUtil.find(content, findType, express, cssType, attrName);
            int resultType = Integer.parseInt(operateDetail.get("resultType"));
            int nextIndex = index + 1;
            if (resultType == 1) {
                // 给下一步处理
                catchStarter.submit(steps, nextIndex, result, variables);
            } else if (resultType == 2) {
                String key = operateDetail.get("key");
                if (CollectionUtils.isEmpty(result)) {
                    log.info("步骤：{}，未找到变量：{}", index, key);
                    return;
                }
                variables.put(key, result.get(0));
                // 给下一步处理
                catchStarter.submit(steps, nextIndex, contents, variables);
            }
        });
    }
}
