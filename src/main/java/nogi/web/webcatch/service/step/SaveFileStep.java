package nogi.web.webcatch.service.step;

import lombok.extern.slf4j.Slf4j;
import nogi.web.webcatch.dto.CatchStep;
import nogi.web.webcatch.service.Downloader;
import nogi.web.webcatch.util.RegUtil;
import nogi.web.webcatch.util.VarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class SaveFileStep extends BaseStep {
    @Autowired
    private Downloader downloader;

    @Override
    public void process(List<Map<String, String>> steps, int index, List<String> contents, Map<String, String> variables) {
        Map<String, String> operateDetail = steps.get(index);
        String downloadType = operateDetail.get("downloadType");


        int fileNameFrom = Integer.parseInt(operateDetail.get("fileNameFrom"));
        String fileName = VarUtil.replaceVar(operateDetail.get("fileName"), variables);
        int fileTypeFrom = Integer.parseInt(operateDetail.get("fileTypeFrom"));
        String fileType = VarUtil.replaceVar(operateDetail.get("fileType"), variables);
        String downloadDir = VarUtil.replaceVar(operateDetail.get("downloadDir"), variables);

        contents.forEach(url -> {
            String finalName = getNameFromUrl(url, fileNameFrom, fileName, fileTypeFrom, fileType, variables);
            String filePath = downloadDir + "\\" + finalName;
            filePath = RegUtil.getLegalName(filePath);
            downloader.download(url, filePath);
        });

    }

    /**
     * 从url获取文件名，包含后缀
     * fileNameFrom 1.文件名-自动获取 2.文件名-随机 3.文件名-自定义
     * fileTypeFrom 1.文件类型-自动获取 2.文件类型-自定义
     */
    private String getNameFromUrl(String url, int fileNameFrom, String definedName, int fileTypeFrom, String definedType, Map<String, String> variables) {
        // 文件名-随机，文件类型-自定义
        if (2 == fileNameFrom && 2 == fileTypeFrom)
            return UUID.randomUUID().toString() + definedType;

        // 从地址中取出文件名称和类型
        String fullNameFromUrl;
        int a = url.lastIndexOf('/');
        int b = url.indexOf('?');
        int c = url.indexOf('#');
        if (-1 == b && -1 == c)
            fullNameFromUrl = url.substring(a + 1);
        else
            fullNameFromUrl = url.substring(a + 1, -1 == b ? c : b);

        // 文件名-自动获取,文件类型-自动获取
        if (1 == fileNameFrom && 1 == fileTypeFrom)
            return fullNameFromUrl;

        int dotIndex = fullNameFromUrl.lastIndexOf('.');
        String fileName;
        String fileType;
        if (-1 == dotIndex) {
            fileName = fullNameFromUrl;
            fileType = "";
        } else {
            fileName = fullNameFromUrl.substring(0, dotIndex);
            fileType = fullNameFromUrl.substring(dotIndex);
        }

        String finalName;
        if (1 == fileNameFrom)
            finalName = fileName;
        else if (2 == fileNameFrom)
            finalName = UUID.randomUUID().toString();
        else
            finalName = definedName;

        return finalName + (1 == fileTypeFrom ? fileType : definedType);
    }
}
