package com.example.certmgmt.domain.model;

/**
 * 证书状态枚举
 * 
 * @author System
 * @since 1.0.0
 */
public enum CertStatus {
    
    /**
     * 正常状态
     */
    NORMAL("Normal", "正常"),
    
    /**
     * 即将过期状态（30天内过期）
     */
    EXPIRING_SOON("ExpiringSoon", "即将过期"),
    
    /**
     * 已过期状态
     */
    EXPIRED("Expired", "已过期");

    private final String code;
    private final String description;

    CertStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取状态枚举
     * 
     * @param code 状态代码
     * @return 状态枚举
     */
    public static CertStatus fromCode(String code) {
        for (CertStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown cert status code: " + code);
    }
}