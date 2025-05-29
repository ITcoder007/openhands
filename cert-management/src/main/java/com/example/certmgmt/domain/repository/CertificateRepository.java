package com.example.certmgmt.domain.repository;

import com.example.certmgmt.domain.model.Certificate;
import com.example.certmgmt.domain.model.CertStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 证书仓库接口
 */
public interface CertificateRepository {
    
    /**
     * 保存证书
     */
    Certificate save(Certificate certificate);
    
    /**
     * 根据ID查找证书
     */
    Optional<Certificate> findById(Long id);
    
    /**
     * 根据域名查找证书
     */
    Optional<Certificate> findByDomainName(String domainName);
    
    /**
     * 分页查询证书列表
     */
    IPage<Certificate> findAll(Page<Certificate> page);
    
    /**
     * 根据状态查询证书列表
     */
    List<Certificate> findByStatus(CertStatus status);
    
    /**
     * 查询即将过期的证书（指定天数内）
     */
    List<Certificate> findExpiringSoon(int days);
    
    /**
     * 查询已过期的证书
     */
    List<Certificate> findExpired();
    
    /**
     * 根据负责人查询证书列表
     */
    List<Certificate> findByOwner(String owner);
    
    /**
     * 更新证书
     */
    Certificate update(Certificate certificate);
    
    /**
     * 根据ID删除证书
     */
    void deleteById(Long id);
    
    /**
     * 批量更新证书状态
     */
    int batchUpdateStatus(List<Long> ids, CertStatus status);
    
    /**
     * 统计各状态证书数量
     */
    long countByStatus(CertStatus status);
    
    /**
     * 检查域名是否已存在
     */
    boolean existsByDomainName(String domainName);
}