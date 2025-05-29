package com.example.certmgmt.infrastructure.persistence.mapper;

import com.example.certmgmt.domain.model.Certificate;
import com.example.certmgmt.domain.model.CertStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 证书Mapper接口
 */
@Mapper
public interface CertificateMapper extends BaseMapper<Certificate> {
    
    /**
     * 查询即将过期的证书（指定天数内）
     */
    @Select("SELECT * FROM certificates WHERE expiration_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL #{days} DAY) AND status != 'Expired'")
    List<Certificate> findExpiringSoon(@Param("days") int days);
    
    /**
     * 查询已过期的证书
     */
    @Select("SELECT * FROM certificates WHERE expiration_date < NOW() AND status != 'Expired'")
    List<Certificate> findExpired();
    
    /**
     * 根据负责人查询证书列表
     */
    @Select("SELECT * FROM certificates WHERE owner = #{owner} ORDER BY expiration_date ASC")
    List<Certificate> findByOwner(@Param("owner") String owner);
    
    /**
     * 根据状态查询证书列表
     */
    @Select("SELECT * FROM certificates WHERE status = #{status} ORDER BY expiration_date ASC")
    List<Certificate> findByStatus(@Param("status") String status);
    
    /**
     * 批量更新证书状态
     */
    @Update("<script>" +
            "UPDATE certificates SET status = #{status}, updated_at = NOW() " +
            "WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);
    
    /**
     * 统计各状态证书数量
     */
    @Select("SELECT COUNT(*) FROM certificates WHERE status = #{status}")
    long countByStatus(@Param("status") String status);
    
    /**
     * 检查域名是否已存在
     */
    @Select("SELECT COUNT(*) > 0 FROM certificates WHERE domain_name = #{domainName}")
    boolean existsByDomainName(@Param("domainName") String domainName);
    
    /**
     * 根据域名查找证书
     */
    @Select("SELECT * FROM certificates WHERE domain_name = #{domainName}")
    Certificate findByDomainName(@Param("domainName") String domainName);
}