package org.m.common.cache;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import org.m.common.entity.dto.GPTDto;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class WxCache {


    public static Map<String, String> accountData = new HashMap<>();

    public static LinkedBlockingQueue<GPTDto> queue = new LinkedBlockingQueue<>();
    public static Map<String, String> gptResult = new HashMap<>();


    static {
        if (FileUtil.exist("data.json")) {
            String openData = FileUtil.readString("data.json", StandardCharsets.UTF_8);
            WxCache.accountData.putAll(JSON.parseObject(openData, Map.class));
        }
    }
}
