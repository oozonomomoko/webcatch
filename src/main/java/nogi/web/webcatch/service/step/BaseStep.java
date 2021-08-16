package nogi.web.webcatch.service.step;

import java.util.List;
import java.util.Map;

/**
 * 步骤处理-基类
 */
public abstract class BaseStep {
    /**
     * @param steps 步骤列表
     * @param index 执行步骤的下标
     * @param contents 待处理内容
     * @param variables 变量
     */
    public abstract void process(List<Map<String, String>> steps, int index, String contents, Map<String, String> variables);

}
