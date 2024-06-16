package org.m.web.config;

import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import org.m.common.entity.po.ConfigPo;
import org.m.web.service.IConfigService;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <desc>
 *
 * </desc>
 *
 * @author maju
 * @createDate 2024/1/16
 */
@Component
@Data
public class Config {
    @Resource
    private IConfigService configService;

    private String token;
    private String appId;
    private String secret;
    private String accessToken;
    private String pythonBin;
    private String pythonPath;
    private String chromiumPath;
    private String pyName = "chatgpt.py";

    @PostConstruct
    private void init() {
        Optional<ConfigPo> oneOpt = configService.getOneOpt(QueryWrapper.create());
        if (oneOpt.isPresent()) {
            ConfigPo configPo = oneOpt.get();
            this.token = configPo.getWxToken();
            this.appId = configPo.getWxAppId();
            this.secret = configPo.getWxAppSecret();
            this.pythonBin = configPo.getPythonBin();
            this.pythonPath = configPo.getPythonPath();
            this.chromiumPath = configPo.getChromiumPath();
            this.pyName = configPo.getPythonName();
        }
    }
}
