package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.CatchStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OperateVariableStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;

    @Override
    public void process(List<Map<String, String>> steps, int index, String content, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        int type = Integer.parseInt(operateDetail.get("type"));
        String key = operateDetail.get("key");
        long value = Long.parseLong(operateDetail.get("value"));
        long limit = Long.parseLong(operateDetail.get("limit"));

        long oriValue;
        String oriValueStr = variables.get(key);
        if (oriValueStr == null) {
            oriValue = 0L;
        } else {
            oriValue = Long.parseLong(oriValueStr);
        }
        long result;
        if (1 == type) {
            result = oriValue + value;
            if (result > limit) {
                log.info("步骤：{}，变量{}达到最大值{}", index, key, limit);
                return;
            }
        } else {
            result = oriValue - value;
            if (result < limit) {
                log.info("步骤：{}，变量{}达到最小值{}", index, key, limit);
                return;
            }
        }

        variables.put(key, String.valueOf(result));
        log.info("步骤：{}，变量运算结果{}：{}", index, key, result);
        int nextIndex = index + 1;
        catchStarter.submit(steps, nextIndex, content, variables);
    }
}
