package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.service.CatchStarter;
import nogi.web.webcatch.service.Downloader;
import nogi.web.webcatch.util.VarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SetPaginationStep extends BaseStep {
    @Autowired
    private CatchStarter catchStarter;

    @Override
    public void process(List<Map<String, String>> steps, int index, List<String> contents, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        int fixedLen;
        String forReplace = "\\{" + operateDetail.get("forReplace") + "\\}";
        String fixedLenStr = operateDetail.get("fixedLen");
        if (fixedLenStr != null && !fixedLenStr.equals("")) {
            fixedLen = Integer.parseInt(fixedLenStr);
        } else {
            fixedLen = 0;
        }
        int min = Integer.parseInt(VarUtil.replaceVar(operateDetail.get("from"), variables));
        int max = Integer.parseInt(VarUtil.replaceVar(operateDetail.get("to"), variables));

        if (min > max) {
            log.info("步骤：{}, 页码大小错误，from:{},to:{}", index, min, max);
            return;
        }

        int nextIndex = index + 1;
        contents.forEach(url -> {
            List<String> temp = new ArrayList<>();
            for (int i = min; i <= max; i++) {
                String nexStr = url.replaceFirst(forReplace, getReplaceTarget(i, fixedLen));
                temp.add(nexStr);
            }
            catchStarter.submit(steps, nextIndex, temp, variables);
        });
    }

    private static String getReplaceTarget(int num, int fixedLen) {
        String result = String.valueOf(num);
        if (fixedLen == 0) {
            return result;
        }
        String tmp = "";
        for (int n = 0; n < fixedLen - result.length(); n++) {
            tmp += "0";
        }
        return tmp + result;
    }

}
