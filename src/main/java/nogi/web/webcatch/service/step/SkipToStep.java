package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.CatchStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 循环，相当于do-while
 */
@Slf4j
@Service
public class SkipToStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;

    @Override
    public void process(List<Map<String, String>> steps, int index, String content, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        // 跳转的步骤
        int stepIdx = Integer.parseInt(operateDetail.get("stepIdx"));
        catchStarter.submit(steps, stepIdx, content, variables);

        int continueFlow = Integer.parseInt(operateDetail.get("continueFlow"));
        if(1 == continueFlow) {
            catchStarter.submit(steps, index + 1, content, variables);
        }
    }
}
