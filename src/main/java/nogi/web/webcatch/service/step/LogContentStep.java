package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.CatchStarter;
import nogi.web.webcatch.service.Downloader;
import nogi.web.webcatch.util.VarUtil;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LogContentStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;

    @Override
    public void process(List<Map<String, String>> steps, int index, String content, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        int type = Integer.parseInt(operateDetail.get("type"));
        String key = VarUtil.replaceVar(operateDetail.get("key"), variables);
        int conFlow = Integer.parseInt(operateDetail.get("continueFlow"));
        if (1 == type) {
            log.info("步骤{}，待处理内容为：{}", index, content);
        } else {
            log.info("步骤{}，变量{}为：{}", index, key, variables.get(key));
        }

        if (1 == conFlow) {
            return;
        }
        int nextIndex = index + 1;
        catchStarter.submit(steps, nextIndex, content, variables);
    }
}
