package com.java.windows.tools.component;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.java.windows.tools.model.AppConfig;
import com.java.windows.tools.model.AppDesc;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.nio.charset.Charset;

@Slf4j
public class AppUtils {

    /**
     * 获取描述信息
     */
    public static AppDesc getDesc() {
        // 读取配置文件
        URL jsonUrl = AppUtils.class.getClassLoader().getResource("app_desc.json");
        if (jsonUrl == null) {
            throw new IllegalArgumentException("找不到文件app_desc.json");
        }
        String jsonString = FileUtil.readString(jsonUrl, Charset.defaultCharset());
        if (StringUtils.isBlank(jsonString)) {
            throw new IllegalArgumentException("文件app_desc.json不能为空");
        }
        return JSONObject.parseObject(jsonString, AppDesc.class);
    }

    /**
     * 获取应用配置
     */
    public static AppConfig getConfig(String filename) {
        // 读取配置文件
        if (!FileUtil.exist(filename)) {
            throw new IllegalArgumentException("找不到文件" + FileUtil.file(filename).getAbsolutePath());
        }
        String jsonString = FileUtil.readString(filename, Charset.defaultCharset());
        if (StringUtils.isBlank(jsonString)) {
            throw new IllegalArgumentException("文件" + filename + "不能为空");
        }
        return JSONObject.parseObject(jsonString, AppConfig.class);
    }
}
