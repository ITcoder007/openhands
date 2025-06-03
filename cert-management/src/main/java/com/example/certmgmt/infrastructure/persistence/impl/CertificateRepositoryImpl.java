package com.example.certmgmt.infrastructure.persistence.impl;

import com.example.certmgmt.domain.model.Certificate;
import com.example.certmgmt.domain.model.CertStatus;
import com.example.certmgmt.domain.repository.CertificateRepository;
import com.example.certmgmt.infrastructure.persistence.mapper.CertificateMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 证书仓库实现类
 */
@Repository
public class CertificateRepositoryImpl implements CertificateRepository {
    
    @Autowired
    private CertificateMapper certificateMapper;
    
    @Override
    public Certificate save(Certificate certificate) {
        if (certificate.getId() == null) {
            // 新增
            certificate.setCreatedAt(LocalDateTime.now());
            certificate.setUpdatedAt(LocalDateTime.now());
            certificateMapper.insert(certificate);
        } else {
            // 更新
            certificate.setUpdatedAt(LocalDateTime.now());
            certificateMapper.updateById(certificate);
        }
        return certificate;
    }
    
    @Override
    public Optional<Certificate> findById(Long id) {
        Certificate certificate = certificateMapper.selectById(id);
        return Optional.ofNullable(certificate);
    }
    
    @Override
    public Optional<Certificate> findByDomainName(String domainName) {
        Certificate certificate = certificateMapper.findByDomainName(domainName);
        return Optional.ofNullable(certificate);
    }
    
    @Override
    public IPage<Certificate> findAll(Page<Certificate> page) {
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("expiration_date");
        return certificateMapper.selectPage(page, queryWrapper);
    }
    
    @Override
    public List<Certificate> findByStatus(CertStatus status) {
        return certificateMapper.findByStatus(status.getCode());
    }
    
    @Override
    public List<Certificate> findExpiringSoon(int days) {
        return certificateMapper.findExpiringSoon(days);
    }
    
    @Override
    public List<Certificate> findExpired() {
        return certificateMapper.findExpired();
    }
    
    @Override
    public List<Certificate> findByOwner(String owner) {
        return certificateMapper.findByOwner(owner);
    }
    
    @Override
    public Certificate update(Certificate certificate) {
        certificate.setUpdatedAt(LocalDateTime.now());
        certificateMapper.updateById(certificate);
        return certificate;
    }
    
    @Override
    public void deleteById(Long id) {
        certificateMapper.deleteById(id);
    }
    
    @Override
    public int batchUpdateStatus(List<Long> ids, CertStatus status) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return certificateMapper.batchUpdateStatus(ids, status.getCode());
    }
    
    @Override
    public long countByStatus(CertStatus status) {
        return certificateMapper.countByStatus(status.getCode());
    }
    
    @Override
    public boolean existsByDomainName(String domainName) {
        return certificateMapper.existsByDomainName(domainName);
    }
}