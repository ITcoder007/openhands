package com.example.certmgmt.interfaces.web.controller;

import com.example.certmgmt.application.service.CertificateApplicationService;
import com.example.certmgmt.domain.model.CertStatus;
import com.example.certmgmt.domain.service.CertificateStatusService;
import com.example.certmgmt.interfaces.dto.request.CertificateCreateRequest;
import com.example.certmgmt.interfaces.dto.request.CertificateUpdateRequest;
import com.example.certmgmt.interfaces.dto.response.CertificateResponse;
import com.example.certmgmt.interfaces.dto.response.ResponseDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 证书管理控制器
 */
@RestController
@RequestMapping("/api/certificates")
@Tag(name = "证书管理", description = "证书的增删改查和状态管理")
public class CertificateController {
    
    @Autowired
    private CertificateApplicationService certificateApplicationService;
    
    /**
     * 创建证书
     */
    @PostMapping
    @Operation(summary = "创建证书", description = "创建新的证书记录")
    public ResponseEntity<ResponseDto<CertificateResponse>> createCertificate(
            @Valid @RequestBody CertificateCreateRequest request) {
        
        CertificateResponse response = certificateApplicationService.createCertificate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success("证书创建成功", response));
    }
    
    /**
     * 根据ID获取证书详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取证书详情", description = "根据证书ID获取证书详细信息")
    public ResponseEntity<ResponseDto<CertificateResponse>> getCertificateById(
            @Parameter(description = "证书ID") @PathVariable Long id) {
        
        Optional<CertificateResponse> certificate = certificateApplicationService.getCertificateById(id);
        if (certificate.isPresent()) {
            return ResponseEntity.ok(ResponseDto.success(certificate.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.error("证书不存在"));
        }
    }
    
    /**
     * 根据域名获取证书详情
     */
    @GetMapping("/domain/{domainName}")
    @Operation(summary = "根据域名获取证书", description = "根据域名获取证书详细信息")
    public ResponseEntity<ResponseDto<CertificateResponse>> getCertificateByDomainName(
            @Parameter(description = "域名") @PathVariable String domainName) {
        
        Optional<CertificateResponse> certificate = certificateApplicationService.getCertificateByDomainName(domainName);
        if (certificate.isPresent()) {
            return ResponseEntity.ok(ResponseDto.success(certificate.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.error("证书不存在"));
        }
    }
    
    /**
     * 分页查询证书列表
     */
    @GetMapping
    @Operation(summary = "分页查询证书列表", description = "分页获取证书列表")
    public ResponseEntity<ResponseDto<IPage<CertificateResponse>>> getCertificates(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        
        IPage<CertificateResponse> certificates = certificateApplicationService.getCertificates(page, size);
        return ResponseEntity.ok(ResponseDto.success(certificates));
    }
    
    /**
     * 根据状态查询证书列表
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询证书", description = "根据证书状态获取证书列表")
    public ResponseEntity<ResponseDto<List<CertificateResponse>>> getCertificatesByStatus(
            @Parameter(description = "证书状态") @PathVariable CertStatus status) {
        
        List<CertificateResponse> certificates = certificateApplicationService.getCertificatesByStatus(status);
        return ResponseEntity.ok(ResponseDto.success(certificates));
    }
    
    /**
     * 根据负责人查询证书列表
     */
    @GetMapping("/owner/{owner}")
    @Operation(summary = "根据负责人查询证书", description = "根据证书负责人获取证书列表")
    public ResponseEntity<ResponseDto<List<CertificateResponse>>> getCertificatesByOwner(
            @Parameter(description = "证书负责人") @PathVariable String owner) {
        
        List<CertificateResponse> certificates = certificateApplicationService.getCertificatesByOwner(owner);
        return ResponseEntity.ok(ResponseDto.success(certificates));
    }
    
    /**
     * 获取即将过期的证书列表
     */
    @GetMapping("/expiring")
    @Operation(summary = "获取即将过期的证书", description = "获取指定天数内即将过期的证书列表")
    public ResponseEntity<ResponseDto<List<CertificateResponse>>> getExpiringSoonCertificates(
            @Parameter(description = "天数，默认30天") @RequestParam(defaultValue = "30") int days) {
        
        List<CertificateResponse> certificates = certificateApplicationService.getExpiringSoonCertificates(days);
        return ResponseEntity.ok(ResponseDto.success(certificates));
    }
    
    /**
     * 获取已过期的证书列表
     */
    @GetMapping("/expired")
    @Operation(summary = "获取已过期的证书", description = "获取所有已过期的证书列表")
    public ResponseEntity<ResponseDto<List<CertificateResponse>>> getExpiredCertificates() {
        
        List<CertificateResponse> certificates = certificateApplicationService.getExpiredCertificates();
        return ResponseEntity.ok(ResponseDto.success(certificates));
    }
    
    /**
     * 更新证书
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新证书", description = "更新证书信息")
    public ResponseEntity<ResponseDto<CertificateResponse>> updateCertificate(
            @Parameter(description = "证书ID") @PathVariable Long id,
            @Valid @RequestBody CertificateUpdateRequest request) {
        
        CertificateResponse response = certificateApplicationService.updateCertificate(id, request);
        return ResponseEntity.ok(ResponseDto.success("证书更新成功", response));
    }
    
    /**
     * 删除证书
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除证书", description = "根据证书ID删除证书")
    public ResponseEntity<ResponseDto<Void>> deleteCertificate(
            @Parameter(description = "证书ID") @PathVariable Long id) {
        
        certificateApplicationService.deleteCertificate(id);
        return ResponseEntity.ok(ResponseDto.success("证书删除成功", null));
    }
    
    /**
     * 获取证书状态统计
     */
    @GetMapping("/statistics")
    @Operation(summary = "获取证书状态统计", description = "获取各状态证书的数量统计")
    public ResponseEntity<ResponseDto<CertificateStatusService.CertificateStatusStatistics>> getStatusStatistics() {
        
        CertificateStatusService.CertificateStatusStatistics statistics = 
                certificateApplicationService.getStatusStatistics();
        return ResponseEntity.ok(ResponseDto.success(statistics));
    }
    
    /**
     * 手动更新所有证书状态
     */
    @PostMapping("/update-status")
    @Operation(summary = "更新所有证书状态", description = "手动触发更新所有证书的状态")
    public ResponseEntity<ResponseDto<Integer>> updateAllCertificateStatus() {
        
        int updatedCount = certificateApplicationService.updateAllCertificateStatus();
        return ResponseEntity.ok(ResponseDto.success("状态更新完成，共更新 " + updatedCount + " 个证书", updatedCount));
    }
}