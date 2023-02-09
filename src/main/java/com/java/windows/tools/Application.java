package com.java.windows.tools;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.java.windows.tools.model.AppConfig;
import com.java.windows.tools.model.BackupConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        try {
            System.out.printf("Application start");
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
            e.printStackTrace();
        }
    }

    /**
     * 备份
     *
     * @param configs
     */
    private static void backup(List<BackupConfig> configs) {
        if (configs != null && !configs.isEmpty()) {
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
                    System.out.printf("原目录/文件source=" + config.getSource() + "复制至目标目录/文件target=" + config.getTarget());
                } else {
                    System.out.printf("原目录/文件source=" + config.getSource() + "不存在");
                }
            }
        } else {
            System.out.printf("备份配置列表为空, 备份组件执行完成");
        }
    }
}
