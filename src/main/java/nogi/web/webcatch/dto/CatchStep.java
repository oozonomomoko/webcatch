package nogi.web.webcatch.dto;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

/**
 * @author 左手掐腰
 * @since 2019/10/11 10:20
 */
@Getter
@Setter
public class CatchStep {

    private JProgressBar progress;

    private CatchStep next;

    /*
     * 资源操作方式
     * 0. 访问资源，直接下载resource
     * 1. 访问资源，获取结果 正则匹配
     * 2. 访问资源，获取结果 css选择器匹配
     * 3. 处理资源地址，不访问
     */
    private int operateType;


    // operateType=0. 直接下载配置---------------------------start
    /*
     * 文件存放目录
      */
    private String downloadDir;

    /*
     * 文件名获取方式,
     * 0:从url中获取，重名时加上随机字符命名
     * 1:随机生成文件名，
     */
    private int fileNameFrom;

    private String fileName;

    /*
     * 文件后缀获取方式
     * 0：从url中获取
     * 1：自定义文件后缀
     */
    private int fileTypeFrom;

    /*
     * 文件后缀，如 .jpg .mp4，可为空
     */
    private String fileType;

    // operateType=0. 直接下载配置---------------------------end



    // operateType=1. 正则匹配结果---------------------------start
    /*
     * 正则表达式
     */
    private String regSelector;

    /**
     * 是否替换到regSource中
     */
    private int regReplace;

    /*
     * 使用正则匹配到的字符替换到此字符串中
     */
    private String regSource;
    // operateType=1. 正则匹配结果---------------------------end


    // operateType=2. css选择器结果---------------------------start
    /*
     * css选择器
     */
    private String cssSelector;

    /*
     * 0 获取标签属性
     * 1 获取innerHTML
     * 2 不处理直接返回
     */
    private int attrType;

    /*
     * attrType=0
     */
    private String attrName;
    // operateType=2. css选择器结果---------------------------end

    private String pageMin;

    private String pageMax;

    private int varOperate;

    private String varName;

    private String varValue;
}
