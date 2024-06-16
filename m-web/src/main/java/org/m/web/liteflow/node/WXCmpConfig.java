package org.m.web.liteflow.node;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.annotation.LiteflowMethod;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.enums.LiteFlowMethodEnum;
import com.yomahub.liteflow.enums.NodeTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.m.common.cache.WxCache;
import org.m.common.entity.dto.WxParamDto;
import org.m.common.enums.KeyTypeEnum;
import org.m.web.service.WxHandleService;

import java.util.Arrays;


@LiteflowComponent
@Slf4j
public class WXCmpConfig {

    @Resource
    private WxHandleService wxHandleService;

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_IF, nodeId = "emptyIF", nodeName = "判断是否为空", nodeType = NodeTypeEnum.IF)
    public boolean processEmpty(NodeComponent bindCmp) {
        WxParamDto wxParamVO = bindCmp.getRequestData();
        boolean result = true;
        if (StrUtil.isEmpty(wxParamVO.getContent())) {
            wxParamVO.setResult("未知指令！");
            result = false;
        }
        return result;
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "returnNode", nodeName = "返回数据", nodeType = NodeTypeEnum.COMMON)
    public void returnNode(NodeComponent bindCmp) {
        WxParamDto wxParamVO = bindCmp.getRequestData();
        log.info(wxParamVO.getResult());
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS_SWITCH, nodeId = "case", nodeName = "指令判断", nodeType = NodeTypeEnum.SWITCH)
    public String caseCmd(NodeComponent bindCmp) {
        WxParamDto wxParamVO = bindCmp.getRequestData();
        KeyTypeEnum keyTypeEnum = KeyTypeEnum.forMsg(wxParamVO.getContent());
        return keyTypeEnum.getCmp();
    }


    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "result", nodeName = "获取结果", nodeType = NodeTypeEnum.COMMON)
    public void processResult(NodeComponent bindCmp) {
        WxParamDto wxParamVO = bindCmp.getRequestData();
        if (!WxCache.gptResult.containsKey(wxParamVO.getFromUserName())) {
            wxParamVO.setResult("请先开始提问！");
            return;
        }
        String f = WxCache.gptResult.get(wxParamVO.getFromUserName());
        if (StrUtil.isBlank(f)) {
            wxParamVO.setResult("正在生成中,请稍后输入获取结果查询,如果文本过长,请多次获取,例如：\n获取结果");
            return;
        }
        String[] split = StrUtil.split(f, 1000);
        String result = split[0];
        if (split.length == 1) {
            WxCache.gptResult.remove(wxParamVO.getFromUserName());
        } else {
            String[] last = Arrays.copyOfRange(split, 1, split.length);
            WxCache.gptResult.put(wxParamVO.getFromUserName(), StrUtil.join("", last));
        }
        wxParamVO.setResult(result);
    }

    @LiteflowMethod(value = LiteFlowMethodEnum.PROCESS, nodeId = "defaultNode", nodeName = "default", nodeType = NodeTypeEnum.COMMON)
    public void defaultNode(NodeComponent bindCmp) {
        WxParamDto wxParamVO = bindCmp.getRequestData();
        if (WxCache.gptResult.containsKey(wxParamVO.getFromUserName())) {
            wxParamVO.setResult("上次结果还在生成中,请稍后再试！");
            return;
        }
        WxCache.gptResult.put(wxParamVO.getFromUserName(), "");
        new Thread(() -> {
            wxHandleService.getPy(wxParamVO.getFromUserName(), wxParamVO.getContent());
        }).start();
        try {
            Thread.sleep(4500);
        } catch (InterruptedException e) {
        }
        if (StrUtil.isNotBlank(WxCache.gptResult.get(wxParamVO.getFromUserName()))) {
            wxParamVO.setResult(WxCache.gptResult.get(wxParamVO.getFromUserName()));
            WxCache.gptResult.remove(wxParamVO.getFromUserName());
            return;
        }
        wxParamVO.setResult("正在生成中,请稍后输入获取结果查询,如果文本过长,请多次获取,例如：\n获取结果");
    }
}
