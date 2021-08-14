package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.CatchStarter;
import nogi.web.webcatch.service.Downloader;
import nogi.web.webcatch.util.RegUtil;
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
public class NextPaginationStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;

    @Autowired
    private Downloader downloader;

    @Override
    public void process(List<Map<String, String>> steps, int index, List<String> contents, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        // 1.正则表达式 2.CSS选择器 3.JPath 4.XPath
        int findType = Integer.parseInt(operateDetail.get("findType"));
        String express = VarUtil.replaceVar(operateDetail.get("express"), variables);
        int cssType = Integer.parseInt(operateDetail.get("cssType"));
        String attrName = VarUtil.replaceVar(operateDetail.get("attrName"), variables);

        int nextIndex = index + 1;
        contents.forEach(url -> {
            Document doc = downloader.getDoc(url);
            if (doc == null) {
                log.info("步骤：{}， 资源访问失败：{}", index, url);
                return;
            }
            // 访问内容交给下一步处理
            catchStarter.submit(steps, nextIndex, Collections.singletonList(doc.toString()), variables);

            // 查找下一页
            List<String> nextUrl = RegUtil.find(doc.toString(), findType, express, cssType, attrName);
            if (CollectionUtils.isEmpty(nextUrl)) {
                log.info("步骤：{}， 未找到下一页地址：{}", index, url);
                return;
            }
            // 下一页链接只有一个
            catchStarter.submit(steps, index, Collections.singletonList(nextUrl.get(0)), variables);
        });
    }
}
