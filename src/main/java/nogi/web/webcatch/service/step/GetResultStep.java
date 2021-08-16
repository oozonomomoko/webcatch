package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.CatchStarter;
import nogi.web.webcatch.service.Downloader;
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
        Document doc = downloader.getDoc(content);
        if (doc == null) {
            log.error("步骤{}，访问链接失败：{}", index, content);
            return;
        }
        int nextIndex = index + 1;
        catchStarter.submit(steps, nextIndex, doc.outerHtml(), variables);
    }
}
