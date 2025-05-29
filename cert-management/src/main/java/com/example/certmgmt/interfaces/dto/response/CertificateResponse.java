package com.example.certmgmt.interfaces.dto.response;

import com.example.certmgmt.domain.model.CertStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 证书响应DTO
 */
@Schema(description = "证书信息响应")
public class CertificateResponse {
    
    @Schema(description = "证书ID", example = "1")
    private Long id;
    
    @Schema(description = "关联域名", example = "example.com")
    private String domainName;
    
    @Schema(description = "备用名称(Subject Alternative Names)", example = "*.example.com,api.example.com")
    private String sans;
    
    @Schema(description = "证书颁发机构", example = "Let's Encrypt")
    private String issuer;
    
    @Schema(description = "证书到期时间", example = "2024-12-31 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationDate;
    
    @Schema(description = "证书负责人", example = "张三")
    private String owner;
    
    @Schema(description = "备注信息", example = "生产环境主域名证书")
    private String notes;
    
    @Schema(description = "证书状态", example = "NORMAL")
    private CertStatus status;
    
    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Schema(description = "最后更新时间", example = "2024-01-01 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Constructors
    public CertificateResponse() {}
    
    // Getters and Setters
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
    
    public CertStatus getStatus() {
        return status;
    }
    
    public void setStatus(CertStatus status) {
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
}