package com.example.certmgmt.interfaces.assembler;

import com.example.certmgmt.domain.model.Certificate;
import com.example.certmgmt.domain.model.CertStatus;
import com.example.certmgmt.interfaces.dto.request.CertificateCreateRequest;
import com.example.certmgmt.interfaces.dto.request.CertificateUpdateRequest;
import com.example.certmgmt.interfaces.dto.response.CertificateResponse;
import org.springframework.stereotype.Component;

/**
 * 证书DTO转换器
 */
@Component
public class CertificateAssembler {
    
    /**
     * 创建请求转换为领域实体
     */
    public Certificate toEntity(CertificateCreateRequest request) {
        Certificate certificate = new Certificate();
        certificate.setDomainName(request.getDomainName());
        certificate.setSans(request.getSans());
        certificate.setIssuer(request.getIssuer());
        certificate.setExpirationDate(request.getExpirationDate());
        certificate.setOwner(request.getOwner());
        certificate.setNotes(request.getNotes());
        return certificate;
    }
    
    /**
     * 更新请求应用到领域实体
     */
    public void updateEntity(Certificate certificate, CertificateUpdateRequest request) {
        certificate.setSans(request.getSans());
        certificate.setIssuer(request.getIssuer());
        certificate.setExpirationDate(request.getExpirationDate());
        certificate.setOwner(request.getOwner());
        certificate.setNotes(request.getNotes());
    }
    
    /**
     * 领域实体转换为响应DTO
     */
    public CertificateResponse toResponse(Certificate certificate) {
        CertificateResponse response = new CertificateResponse();
        response.setId(certificate.getId());
        response.setDomainName(certificate.getDomainName());
        response.setSans(certificate.getSans());
        response.setIssuer(certificate.getIssuer());
        response.setExpirationDate(certificate.getExpirationDate());
        response.setOwner(certificate.getOwner());
        response.setNotes(certificate.getNotes());
        response.setStatus(CertStatus.valueOf(certificate.getStatus()));
        response.setCreatedAt(certificate.getCreatedAt());
        response.setUpdatedAt(certificate.getUpdatedAt());
        return response;
    }
}