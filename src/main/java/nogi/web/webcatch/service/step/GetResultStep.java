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
    public void process(List<Map<String, String>> steps, int index, List<String> contents, Map<String, String> variables) {
        int nextIndex = index + 1;
        contents.forEach(url -> {
            Document doc = downloader.getDoc(url);
            if (doc == null) {
                return;
            }
            catchStarter.submit(steps, nextIndex, Collections.singletonList(doc.outerHtml()), variables);
        });
    }
}
