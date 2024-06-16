package org.m.pay.service;

import jakarta.servlet.http.HttpServletRequest;
import org.m.common.entity.dto.PayDto;
import org.m.common.entity.dto.TaskExecuteDto;

public interface IPayServer {

    PayDto getPay(HttpServletRequest request);

    PayDto pay(HttpServletRequest request, PayDto dto);

    void onCallBack(TaskExecuteDto payDto);

}
