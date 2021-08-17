package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.CatchStarter;
import nogi.web.webcatch.util.VarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReplaceStringStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;

    @Override
    public void process(List<Map<String, String>> steps, int index, String content, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        // 1.待处理内容 2.变量
        int replaceObj = Integer.parseInt(operateDetail.get("replaceObj"));

        // 1.正则表达式 2.文本内容
        int replaceType = Integer.parseInt(operateDetail.get("replaceType"));
        String express = VarUtil.replaceVar(operateDetail.get("express"), variables);
        String target = VarUtil.replaceVar(operateDetail.get("target"), variables);
        int nextIndex = index + 1;

        if (1 == replaceObj) {
            if (1 == replaceType) {
                catchStarter.submit(steps, nextIndex, content.replaceAll(express, target), variables);
            } else {
                catchStarter.submit(steps, nextIndex, content.replace(express, target), variables);
            }
        } else {
            String varName = VarUtil.replaceVar(operateDetail.get("varName"), variables);
            String value = variables.get(varName);
            if (value != null) {
                if (1 == replaceType) {
                    value = value.replaceAll(express, target);
                } else {
                    value = value.replace(express, target);
                }
                variables.put(varName, value);
            } else {
                log.info("步骤{}，未发现要替换内容字符的变量：{}，继续执行", index, varName);
            }
            catchStarter.submit(steps, nextIndex, content.replace(express, target), variables);

        }
    }
}
