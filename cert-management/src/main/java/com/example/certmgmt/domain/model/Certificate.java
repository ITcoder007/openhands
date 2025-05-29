package com.example.certmgmt.domain.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 证书领域实体
 * 
 * @author System
 * @since 1.0.0
 */
@TableName("certificates")
public class Certificate {

    /**
     * 证书ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联域名
     */
    @TableField("domain_name")
    private String domainName;

    /**
     * 备用名称(Subject Alternative Names)
     */
    @TableField("sans")
    private String sans;

    /**
     * 证书颁发机构
     */
    @TableField("issuer")
    private String issuer;

    /**
     * 证书到期时间
     */
    @TableField("expiration_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationDate;

    /**
     * 证书负责人
     */
    @TableField("owner")
    private String owner;

    /**
     * 备注信息
     */
    @TableField("notes")
    private String notes;

    /**
     * 证书状态
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 最后更新时间
     */
    @TableField("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // 构造函数
    public Certificate() {
    }

    public Certificate(String domainName, String sans, String issuer, 
                      LocalDateTime expirationDate, String owner, String notes) {
        this.domainName = domainName;
        this.sans = sans;
        this.issuer = issuer;
        this.expirationDate = expirationDate;
        this.owner = owner;
        this.notes = notes;
        this.status = calculateStatus().getCode();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 计算证书状态
     * 
     * @return 证书状态
     */
    public CertStatus calculateStatus() {
        return calculateStatus(30);
    }
    
    /**
     * 计算证书状态
     * 
     * @param expiringSoonDays 即将过期天数阈值
     * @return 证书状态
     */
    public CertStatus calculateStatus(int expiringSoonDays) {
        if (expirationDate == null) {
            return CertStatus.NORMAL;
        }

        LocalDate today = LocalDate.now();
        LocalDate expirationLocalDate = expirationDate.toLocalDate();

        // 已过期
        if (today.isAfter(expirationLocalDate)) {
            return CertStatus.EXPIRED;
        }

        // 指定天数内过期视为即将过期
        LocalDate warnDate = expirationLocalDate.minusDays(expiringSoonDays);
        if (today.isAfter(warnDate) || today.isEqual(warnDate)) {
            return CertStatus.EXPIRING_SOON;
        }

        return CertStatus.NORMAL;
    }

    /**
     * 判断证书是否已过期
     * 
     * @return true if expired
     */
    public boolean isExpired() {
        return calculateStatus() == CertStatus.EXPIRED;
    }

    /**
     * 判断证书是否即将过期
     * 
     * @return true if expiring soon
     */
    public boolean isExpiringSoon() {
        return calculateStatus() == CertStatus.EXPIRING_SOON;
    }

    /**
     * 获取距离过期的天数
     * 
     * @return 距离过期的天数，负数表示已过期
     */
    public long getDaysUntilExpiration() {
        if (expirationDate == null) {
            return Long.MAX_VALUE;
        }
        LocalDate today = LocalDate.now();
        LocalDate expirationLocalDate = expirationDate.toLocalDate();
        return ChronoUnit.DAYS.between(today, expirationLocalDate);
    }

    /**
     * 更新证书状态
     */
    public void updateStatus() {
        this.status = calculateStatus().getCode();
        this.updatedAt = LocalDateTime.now();
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getSans() {
        return sans;
    }

    public void setSans(String sans) {
        this.sans = sans;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", domainName='" + domainName + '\'' +
                ", sans='" + sans + '\'' +
                ", issuer='" + issuer + '\'' +
                ", expirationDate=" + expirationDate +
                ", owner='" + owner + '\'' +
                ", notes='" + notes + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}