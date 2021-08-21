package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.CatchStarter;
import nogi.web.webcatch.util.RegUtil;
import nogi.web.webcatch.util.VarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class FindContentStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;

    @Override
    public void process(List<Map<String, String>> steps, int index, String content, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        // 1.正则表达式 2.CSS选择器 3.JPath 4.XPath
        int findType = Integer.parseInt(operateDetail.get("findType"));
        String express = VarUtil.replaceVar(operateDetail.get("express"), variables);
        int cssType = Integer.parseInt(operateDetail.get("cssType"));
        String attrName = VarUtil.replaceVar(operateDetail.get("attrName"), variables);

        int nextIndex = index + 1;
        List<String> result = RegUtil.find(content, findType, express, cssType, attrName);

        // 匹配结果result给下一步处理
        catchStarter.submit(steps, nextIndex, new ArrayList<>(new HashSet<>(result)), variables);
    }
}
