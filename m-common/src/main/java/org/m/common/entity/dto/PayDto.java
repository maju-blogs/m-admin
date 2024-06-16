package org.m.common.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayDto {
    private String payId;
    private String payIp;
    private Integer payType;
    private Date payDate;
    private Date payTimeOutDate;
    private Date payDelDate;
    private BigDecimal payAmount;
    private String payDesc;
    private String payUserName;
    private Integer payStatus;
    private String payTaskId;
    private Integer payQrId;
    private String payQrBase64;
}
