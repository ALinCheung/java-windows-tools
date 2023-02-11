package com.java.windows.tools.component;

import cn.hutool.core.io.FileUtil;
import com.java.windows.tools.model.BackupConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

@Slf4j
public class BackupUtils {

    /**
     * 备份
     *
     * @param configs
     */
    public static String execute(List<BackupConfig> configs) {
        String msg = "";
        if (configs != null && !configs.isEmpty()) {
            log.info("===== 备份组件执行开始 =====");
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
                    log.info("原目录/文件source={}复制至目标目录/文件target={}", config.getSource(), config.getTarget());
                } else {
                    msg += "原目录/文件source=" + config.getSource() + "不存在;";
                    log.error("原目录/文件source={}不存在", config.getSource());
                }
            }
            log.info("===== 备份组件执行结束 =====");
        } else {
            msg += "备份配置列表为空";
            log.info("===== 备份配置列表为空, 备份组件执行跳过 =====");
        }
        return msg;
    }
}
