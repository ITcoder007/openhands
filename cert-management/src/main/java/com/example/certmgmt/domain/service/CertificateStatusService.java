package com.example.certmgmt.domain.service;

import com.example.certmgmt.domain.model.Certificate;
import com.example.certmgmt.domain.model.CertStatus;
import com.example.certmgmt.domain.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 证书状态领域服务
 */
@Service
public class CertificateStatusService {
    
    @Autowired
    private CertificateRepository certificateRepository;
    
    @Value("${cert.expiring-soon-days:30}")
    private int expiringSoonDays;
    
    /**
     * 更新所有证书状态
     * @return 更新的证书数量
     */
    public int updateAllCertificateStatus() {
        int updatedCount = 0;
        
        // 更新已过期证书状态
        List<Certificate> expiredCerts = certificateRepository.findExpired();
        if (!expiredCerts.isEmpty()) {
            List<Long> expiredIds = expiredCerts.stream()
                    .filter(cert -> !CertStatus.EXPIRED.getCode().equals(cert.getStatus()))
                    .map(Certificate::getId)
                    .collect(Collectors.toList());
            
            if (!expiredIds.isEmpty()) {
                updatedCount += certificateRepository.batchUpdateStatus(expiredIds, CertStatus.EXPIRED);
            }
        }
        
        // 更新即将过期证书状态
        List<Certificate> expiringSoonCerts = certificateRepository.findExpiringSoon(expiringSoonDays);
        if (!expiringSoonCerts.isEmpty()) {
            List<Long> expiringSoonIds = expiringSoonCerts.stream()
                    .filter(cert -> CertStatus.NORMAL.getCode().equals(cert.getStatus()))
                    .map(Certificate::getId)
                    .collect(Collectors.toList());
            
            if (!expiringSoonIds.isEmpty()) {
                updatedCount += certificateRepository.batchUpdateStatus(expiringSoonIds, CertStatus.EXPIRING_SOON);
            }
        }
        
        return updatedCount;
    }
    
    /**
     * 计算证书状态
     */
    public CertStatus calculateCertificateStatus(Certificate certificate) {
        return certificate.calculateStatus(expiringSoonDays);
    }
    
    /**
     * 获取证书状态统计
     */
    public CertificateStatusStatistics getStatusStatistics() {
        long normalCount = certificateRepository.countByStatus(CertStatus.NORMAL);
        long expiringSoonCount = certificateRepository.countByStatus(CertStatus.EXPIRING_SOON);
        long expiredCount = certificateRepository.countByStatus(CertStatus.EXPIRED);
        
        return new CertificateStatusStatistics(normalCount, expiringSoonCount, expiredCount);
    }
    
    /**
     * 证书状态统计内部类
     */
    public static class CertificateStatusStatistics {
        private final long normalCount;
        private final long expiringSoonCount;
        private final long expiredCount;
        
        public CertificateStatusStatistics(long normalCount, long expiringSoonCount, long expiredCount) {
            this.normalCount = normalCount;
            this.expiringSoonCount = expiringSoonCount;
            this.expiredCount = expiredCount;
        }
        
        public long getNormalCount() {
            return normalCount;
        }
        
        public long getExpiringSoonCount() {
            return expiringSoonCount;
        }
        
        public long getExpiredCount() {
            return expiredCount;
        }
        
        public long getTotalCount() {
            return normalCount + expiringSoonCount + expiredCount;
        }
    }
}