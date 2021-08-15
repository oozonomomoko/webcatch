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
public class LoopStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;

    @Override
    public void process(List<Map<String, String>> steps, int index, List<String> contents, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        // 要循环的步骤个数
        int loopCount = Integer.parseInt(operateDetail.get("loopCount"));
        // 先将待处理字符交给循环外下一步处理
        catchStarter.submit(steps, index + 1, contents, variables);
        // 再返回循环起始处
        if (index < loopCount) {
            log.info("步骤{}，回退步骤过多，超出范围", index);
            return;
        }
        catchStarter.submit(steps, index - loopCount, contents, variables);
    }
}
