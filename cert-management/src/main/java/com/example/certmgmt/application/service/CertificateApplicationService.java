package com.example.certmgmt.application.service;

import com.example.certmgmt.domain.model.Certificate;
import com.example.certmgmt.domain.model.CertStatus;
import com.example.certmgmt.domain.repository.CertificateRepository;
import com.example.certmgmt.domain.service.CertificateStatusService;
import com.example.certmgmt.interfaces.assembler.CertificateAssembler;
import com.example.certmgmt.interfaces.dto.request.CertificateCreateRequest;
import com.example.certmgmt.interfaces.dto.request.CertificateUpdateRequest;
import com.example.certmgmt.interfaces.dto.response.CertificateResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 证书应用服务
 */
@Service
@Transactional
public class CertificateApplicationService {
    
    @Autowired
    private CertificateRepository certificateRepository;
    
    @Autowired
    private CertificateStatusService certificateStatusService;
    
    @Autowired
    private CertificateAssembler certificateAssembler;
    
    /**
     * 创建证书
     */
    public CertificateResponse createCertificate(CertificateCreateRequest request) {
        // 检查域名是否已存在
        if (certificateRepository.existsByDomainName(request.getDomainName())) {
            throw new IllegalArgumentException("域名 " + request.getDomainName() + " 已存在");
        }
        
        // 转换为领域实体
        Certificate certificate = certificateAssembler.toEntity(request);
        
        // 计算证书状态
        CertStatus status = certificateStatusService.calculateCertificateStatus(certificate);
        certificate.setStatus(status.getCode());
        
        // 保存证书
        Certificate savedCertificate = certificateRepository.save(certificate);
        
        // 转换为响应DTO
        return certificateAssembler.toResponse(savedCertificate);
    }
    
    /**
     * 根据ID获取证书详情
     */
    @Transactional(readOnly = true)
    public Optional<CertificateResponse> getCertificateById(Long id) {
        return certificateRepository.findById(id)
                .map(certificateAssembler::toResponse);
    }
    
    /**
     * 根据域名获取证书详情
     */
    @Transactional(readOnly = true)
    public Optional<CertificateResponse> getCertificateByDomainName(String domainName) {
        return certificateRepository.findByDomainName(domainName)
                .map(certificateAssembler::toResponse);
    }
    
    /**
     * 分页查询证书列表
     */
    @Transactional(readOnly = true)
    public IPage<CertificateResponse> getCertificates(int page, int size) {
        Page<Certificate> pageRequest = new Page<>(page, size);
        IPage<Certificate> certificatePage = certificateRepository.findAll(pageRequest);
        
        return certificatePage.convert(certificateAssembler::toResponse);
    }
    
    /**
     * 根据状态查询证书列表
     */
    @Transactional(readOnly = true)
    public List<CertificateResponse> getCertificatesByStatus(CertStatus status) {
        List<Certificate> certificates = certificateRepository.findByStatus(status);
        return certificates.stream()
                .map(certificateAssembler::toResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据负责人查询证书列表
     */
    @Transactional(readOnly = true)
    public List<CertificateResponse> getCertificatesByOwner(String owner) {
        List<Certificate> certificates = certificateRepository.findByOwner(owner);
        return certificates.stream()
                .map(certificateAssembler::toResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 更新证书
     */
    public CertificateResponse updateCertificate(Long id, CertificateUpdateRequest request) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("证书不存在，ID: " + id));
        
        // 应用更新
        certificateAssembler.updateEntity(certificate, request);
        
        // 重新计算证书状态
        CertStatus status = certificateStatusService.calculateCertificateStatus(certificate);
        certificate.setStatus(status.getCode());
        
        // 保存更新
        Certificate updatedCertificate = certificateRepository.update(certificate);
        
        // 转换为响应DTO
        return certificateAssembler.toResponse(updatedCertificate);
    }
    
    /**
     * 删除证书
     */
    public void deleteCertificate(Long id) {
        if (!certificateRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("证书不存在，ID: " + id);
        }
        
        certificateRepository.deleteById(id);
    }
    
    /**
     * 获取证书状态统计
     */
    @Transactional(readOnly = true)
    public CertificateStatusService.CertificateStatusStatistics getStatusStatistics() {
        return certificateStatusService.getStatusStatistics();
    }
    
    /**
     * 手动更新所有证书状态
     */
    public int updateAllCertificateStatus() {
        return certificateStatusService.updateAllCertificateStatus();
    }
}