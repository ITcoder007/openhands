package com.example.certmgmt.infrastructure.task;

import com.example.certmgmt.domain.service.CertificateStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 证书状态更新定时任务
 */
@Component
public class CertificateStatusUpdateTask {
    
    private static final Logger logger = LoggerFactory.getLogger(CertificateStatusUpdateTask.class);
    
    @Autowired
    private CertificateStatusService certificateStatusService;
    
    /**
     * 每日凌晨1点执行证书状态更新任务
     * cron表达式: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateCertificateStatus() {
        logger.info("开始执行证书状态更新定时任务");
        
        try {
            int updatedCount = certificateStatusService.updateAllCertificateStatus();
            logger.info("证书状态更新完成，共更新 {} 个证书", updatedCount);
            
            // 记录状态统计
            CertificateStatusService.CertificateStatusStatistics statistics = 
                    certificateStatusService.getStatusStatistics();
            logger.info("当前证书状态统计 - 正常: {}, 即将过期: {}, 已过期: {}, 总计: {}", 
                    statistics.getNormalCount(),
                    statistics.getExpiringSoonCount(),
                    statistics.getExpiredCount(),
                    statistics.getTotalCount());
            
        } catch (Exception e) {
            logger.error("证书状态更新定时任务执行失败", e);
        }
    }
    
    /**
     * 每小时执行一次状态统计日志记录（用于监控）
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void logCertificateStatistics() {
        try {
            CertificateStatusService.CertificateStatusStatistics statistics = 
                    certificateStatusService.getStatusStatistics();
            
            if (statistics.getExpiredCount() > 0 || statistics.getExpiringSoonCount() > 0) {
                logger.warn("证书状态警告 - 即将过期: {}, 已过期: {}", 
                        statistics.getExpiringSoonCount(), statistics.getExpiredCount());
            } else {
                logger.debug("证书状态正常 - 总计: {} 个证书", statistics.getTotalCount());
            }
            
        } catch (Exception e) {
            logger.error("证书状态统计记录失败", e);
        }
    }
}