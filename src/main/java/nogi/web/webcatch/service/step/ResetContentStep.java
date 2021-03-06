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
public class ResetContentStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;
    @Override
    public void process(List<Map<String, String>> steps, int index, String content, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        String resetContent = VarUtil.replaceVar(operateDetail.get("content"), variables);

        // 给下一步处理
        int nextIndex = index + 1;
        catchStarter.submit(steps, nextIndex, resetContent, variables);
    }
}
