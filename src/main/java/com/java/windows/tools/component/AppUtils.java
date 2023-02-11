package com.java.windows.tools.component;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.java.windows.tools.Application;
import com.java.windows.tools.model.AppConfig;
import com.java.windows.tools.model.AppDesc;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.function.Consumer;

@Slf4j
public class AppUtils {

    /**
     * 获取描述信息
     */
    public static AppDesc getDesc() {
        return AppUtils.getFile("app_desc.json", AppDesc.class);
    }

    /**
     * 获取应用配置
     */
    public static AppConfig getConfig() {
        return AppUtils.getFile("app_config.json", AppConfig.class);
    }

    /**
     * 获取文件
     * @param filename
     * @param clz
     * @param <T>
     * @return
     */
    private static <T> T getFile(String filename, Class<T> clz) {
        // 读取配置文件
        URL jsonUrl = AppUtils.class.getClassLoader().getResource(filename);
        if (jsonUrl == null) {
            throw new IllegalArgumentException("找不到文件" + filename);
        }
        String jsonString = FileUtil.readString(jsonUrl, Charset.defaultCharset());
        if (StringUtils.isBlank(jsonString)) {
            throw new IllegalArgumentException("文件"+filename+"不能为空");
        }
        return JSONObject.parseObject(jsonString, clz);
    }
}
