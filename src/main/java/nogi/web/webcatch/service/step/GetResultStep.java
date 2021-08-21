package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.CatchStarter;
import nogi.web.webcatch.service.Downloader;
import nogi.web.webcatch.util.VarUtil;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@Service
public class GetResultStep extends BaseStep {
    @Autowired
    private Downloader downloader;

    @Autowired
    private CatchStarter catchStarter;

    @Override
    public void process(List<Map<String, String>> steps, int index, String content, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        int requestType = Integer.parseInt(operateDetail.get("requestType"));

        int resultType = Integer.parseInt(operateDetail.get("resultType"));
        String body = 1 == requestType ? null : VarUtil.replaceVar(operateDetail.get("body"), variables);
        String resultContent;
        if (1 == resultType) {
            Document doc = downloader.getDoc(content, body);
            resultContent = doc.outerHtml();
        } else {
            resultContent = downloader.getJson(content, body);
        }

        if (resultContent == null) {
            log.error("步骤{}，访问链接失败：{}", index, content);
            return;
        }
        int nextIndex = index + 1;
        catchStarter.submit(steps, nextIndex, resultContent, variables);
    }
}
