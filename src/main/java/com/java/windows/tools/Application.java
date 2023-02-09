package com.java.windows.tools;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.java.windows.tools.model.AppConfig;
import com.java.windows.tools.model.BackupConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class Application {

    public static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            logger.info("Application start");
            // 读取配置文件
            URL appJsonUrl = Application.class.getClassLoader().getResource("app.json");
            if (appJsonUrl == null) {
                throw new IllegalArgumentException("找不到应用配置文件app.json");
            }
            String appJsonString = FileUtil.readString(appJsonUrl, Charset.defaultCharset());
            if (StringUtils.isBlank(appJsonString)) {
                throw new IllegalArgumentException("应用配置文件app.json不能为空");
            }
            AppConfig appConfig = JSONObject.parseObject(appJsonString, AppConfig.class);
            if (appConfig != null) {
                // 备份
                Application.backup(appConfig.getBackup());
            }
        } catch (Exception e) {
            logger.error("执行异常, 原因: {}", e.getMessage(), e);
        }
    }

    /**
     * 备份
     *
     * @param configs
     */
    private static void backup(List<BackupConfig> configs) {
        if (configs != null && !configs.isEmpty()) {
            logger.info("===== 备份组件执行开始 =====");
            for (BackupConfig config : configs) {
                if (StringUtils.isNotBlank(config.getSource())
                        && StringUtils.isNotBlank(config.getTarget())
                        && FileUtil.exist(config.getSource())) {
                    File sourceFile = FileUtil.file(config.getSource());
                    String target = config.getTarget();
                    if (FileUtil.isDirectory(config.getTarget())) {
                        target = FileUtil.file(target).getAbsolutePath() + "/" + sourceFile.getName();
                    }
                    FileUtil.copy(sourceFile.getAbsolutePath(), target, true);
                    logger.error("原目录/文件source={}复制至目标目录/文件target={}", config.getSource(), config.getTarget());
                } else {
                    logger.error("原目录/文件source={}不存在", config.getSource());
                }
            }
            logger.info("===== 备份组件执行结束 =====");
        } else {
            logger.info("===== 备份配置列表为空, 备份组件执行跳过 =====");
        }
    }
}
