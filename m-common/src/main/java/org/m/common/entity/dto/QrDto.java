package org.m.common.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrDto {
    private String qrBase64;
    private String qrMark;
    private Integer qrType;
    private Integer qrId;
    private BigDecimal amount;
    private boolean isLock;
    private long timeoutLock;
    private long lastUseTime = System.currentTimeMillis();
}
