package com.example.certmgmt.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 证书更新请求DTO
 */
@Schema(description = "证书更新请求")
public class CertificateUpdateRequest {
    
    @Schema(description = "备用名称(Subject Alternative Names)", example = "*.example.com,api.example.com")
    @Size(max = 2000, message = "备用名称长度不能超过2000个字符")
    private String sans;
    
    @Schema(description = "证书颁发机构", example = "Let's Encrypt", required = true)
    @NotBlank(message = "证书颁发机构不能为空")
    @Size(max = 255, message = "证书颁发机构长度不能超过255个字符")
    private String issuer;
    
    @Schema(description = "证书到期时间", example = "2024-12-31 23:59:59", required = true)
    @NotNull(message = "证书到期时间不能为空")
    private LocalDateTime expirationDate;
    
    @Schema(description = "证书负责人", example = "张三", required = true)
    @NotBlank(message = "证书负责人不能为空")
    @Size(max = 255, message = "证书负责人长度不能超过255个字符")
    private String owner;
    
    @Schema(description = "备注信息", example = "生产环境主域名证书")
    @Size(max = 2000, message = "备注信息长度不能超过2000个字符")
    private String notes;
    
    // Getters and Setters
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
}