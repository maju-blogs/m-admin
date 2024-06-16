package org.m.web.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.m.common.cache.WxCache;
import org.m.web.config.Config;
import org.m.web.util.ShellUtil;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * <desc>
 *
 * </desc>
 *
 * @author maju
 * @createDate 2024/1/18
 */
@Service
@Slf4j
public class WxHandleService {
    @Resource
    private Config config;

    public void getPy(String id, String question) {
        try {
            String exePy = config.getPythonPath() + config.getPyName();
            log.info("开始调用chatgpt:{},py:{}", question, exePy);
            String[] params = {ShellUtil.getExePath(config.getPythonBin(), exePy), exePy, config.getChromiumPath(), question};
            ProcessBuilder processBuilder = new ProcessBuilder(params);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
            log.info("chatgpt调用成功:{}", output);
            WxCache.gptResult.put(id, output.toString().split("RESULT\\|")[1]);
        } catch (Exception e) {
            log.error("调用chatgpt异常", e);
        }
    }
}
