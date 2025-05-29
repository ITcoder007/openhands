# 证书资产管理系统

## 项目概述

基于Spring Boot的证书资产管理系统，采用DDD（领域驱动设计）架构，用于管理域名证书的生命周期。

## 技术栈

- **Java**: 1.8
- **Spring Boot**: 2.5.14
- **MyBatis Plus**: 3.4.3.4
- **数据库**: MySQL 5.7+/8.0+
- **API文档**: Springdoc OpenAPI (Swagger UI)
- **构建工具**: Maven 3.6+

## 项目结构

```
src/main/java/com/example/certmgmt/
├── CertManagementApplication.java          # 主应用类
├── interfaces/                             # 接口层/表现层
│   ├── web/controller/                     # REST控制器
│   ├── dto/
│   │   ├── request/                        # 请求DTO
│   │   └── response/                       # 响应DTO
│   │       └── ResponseDto.java            # 统一响应格式
│   └── assembler/                          # DTO转换器
├── application/                            # 应用层
│   ├── service/                            # 应用服务
│   ├── dto/                                # 应用层DTO
│   └── assembler/                          # 转换器
├── domain/                                 # 领域层
│   ├── model/                              # 领域实体
│   │   ├── Certificate.java                # 证书实体
│   │   └── CertStatus.java                 # 证书状态枚举
│   ├── service/                            # 领域服务
│   ├── repository/                         # 仓库接口
│   └── event/                              # 领域事件
└── infrastructure/                         # 基础设施层
    ├── persistence/
    │   ├── mapper/                         # MyBatis Mapper
    │   └── impl/                           # 仓库实现
    ├── config/                             # 配置类
    │   ├── MyBatisPlusConfig.java          # MyBatis Plus配置
    │   └── SwaggerConfig.java              # Swagger配置
    ├── task/                               # 定时任务
    └── util/                               # 工具类
```

## 核心功能

### 已实现
- [x] 项目基础架构搭建
- [x] Certificate实体类（包含状态计算逻辑）
- [x] CertStatus枚举（Normal/ExpiringSoon/Expired）
- [x] 统一响应格式
- [x] Swagger配置
- [x] MyBatis Plus配置
- [x] Maven项目配置（使用阿里云镜像）

### 待实现
- [ ] 证书CRUD API接口
- [ ] 证书状态定时更新任务
- [ ] 数据库表创建脚本
- [ ] 全局异常处理
- [ ] 参数校验

## 数据模型

### 证书表 (certificates)
```sql
CREATE TABLE `certificates` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '证书ID',
  `domain_name` varchar(255) NOT NULL COMMENT '关联域名',
  `sans` text DEFAULT NULL COMMENT '备用名称(Subject Alternative Names)',
  `issuer` varchar(255) NOT NULL COMMENT '证书颁发机构',
  `expiration_date` datetime NOT NULL COMMENT '证书到期时间',
  `owner` varchar(255) NOT NULL COMMENT '证书负责人',
  `notes` text DEFAULT NULL COMMENT '备注信息',
  `status` varchar(20) NOT NULL DEFAULT 'Normal' COMMENT '证书状态: Normal, ExpiringSoon, Expired',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_domain_name` (`domain_name`),
  INDEX `idx_expiration_date` (`expiration_date`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='证书信息表';
```

## 配置说明

### 数据库配置
在 `application.yml` 中配置MySQL连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cert_management?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false
    username: root
    password: root
```

### API文档
启动应用后，访问 Swagger UI：
- API文档: http://localhost:8080/api-docs
- Swagger UI: http://localhost:8080/swagger-ui.html

## 编译和运行

### 编译项目
```bash
mvn clean compile
```

### 打包项目
```bash
mvn clean package
```

### 运行项目
```bash
mvn spring-boot:run
# 或
java -jar target/cert-management-1.0.0-SNAPSHOT.jar
```

## 开发规范

- 遵循DDD分层架构
- 使用构造函数注入
- 统一的API响应格式
- 日期时间格式：`yyyy-MM-dd HH:mm:ss`
- RESTful API设计
- 完整的Swagger文档

## 状态说明

✅ **当前状态**: 项目基础架构已完成，可以成功编译
🔄 **下一步**: 实现证书CRUD API接口和定时任务

---

*本项目严格按照PRD、架构设计文档和编码规范文档要求开发*