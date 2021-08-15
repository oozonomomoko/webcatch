package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.CatchStarter;
import nogi.web.webcatch.service.Downloader;
import nogi.web.webcatch.util.VarUtil;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SetHeaderStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;

    @Override
    public void process(List<Map<String, String>> steps, int index, List<String> contents, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        String key = VarUtil.replaceVar(operateDetail.get("key"), variables);
        String value = VarUtil.replaceVar(operateDetail.get("value"), variables);
        Downloader.headers.put(key, value);
        int nextIndex = index + 1;
        catchStarter.submit(steps, nextIndex, contents, variables);
    }
}
