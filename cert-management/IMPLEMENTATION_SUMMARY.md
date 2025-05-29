# 证书管理系统实现总结

## 项目概述

基于Spring Boot 2.7.18和Java 8开发的证书资产管理系统，采用DDD（领域驱动设计）分层架构，实现了证书的完整生命周期管理。

## 技术栈

- **Java**: 8
- **Spring Boot**: 2.7.18
- **MyBatis Plus**: 3.5.3.1
- **MySQL**: 8.0.33
- **Swagger**: SpringDoc OpenAPI 3.0
- **Maven**: 3.6+

## 项目结构

```
src/main/java/com/example/certmgmt/
├── CertManagementApplication.java          # 主启动类
├── application/                            # 应用层
│   └── service/
│       └── CertificateApplicationService.java
├── domain/                                 # 领域层
│   ├── model/
│   │   ├── Certificate.java               # 证书实体
│   │   └── CertStatus.java               # 证书状态枚举
│   ├── repository/
│   │   └── CertificateRepository.java    # 仓储接口
│   └── service/
│       └── CertificateStatusService.java # 领域服务
├── infrastructure/                        # 基础设施层
│   ├── config/
│   │   ├── MyBatisPlusConfig.java        # MyBatis配置
│   │   └── SwaggerConfig.java            # Swagger配置
│   ├── exception/
│   │   └── GlobalExceptionHandler.java   # 全局异常处理
│   ├── persistence/
│   │   ├── impl/
│   │   │   └── CertificateRepositoryImpl.java
│   │   └── mapper/
│   │       └── CertificateMapper.java    # MyBatis映射器
│   └── task/
│       └── CertificateStatusUpdateTask.java # 定时任务
└── interfaces/                           # 接口层
    ├── assembler/
    │   └── CertificateAssembler.java     # DTO转换器
    ├── dto/
    │   ├── request/
    │   │   ├── CertificateCreateRequest.java
    │   │   └── CertificateUpdateRequest.java
    │   └── response/
    │       ├── CertificateResponse.java
    │       └── ResponseDto.java          # 统一响应格式
    └── web/
        └── controller/
            └── CertificateController.java # REST控制器
```

## 核心功能实现

### 1. 证书CRUD操作

- **创建证书**: `POST /api/certificates`
- **查询证书列表**: `GET /api/certificates`
- **查询证书详情**: `GET /api/certificates/{id}`
- **更新证书**: `PUT /api/certificates/{id}`
- **删除证书**: `DELETE /api/certificates/{id}`

### 2. 证书状态管理

#### 状态枚举
- `NORMAL(1)`: 正常状态
- `EXPIRING_SOON(2)`: 即将过期（30天内）
- `EXPIRED(3)`: 已过期

#### 自动状态计算
- 创建/更新证书时自动计算状态
- 基于过期时间和当前时间比较
- 支持配置即将过期天数阈值

### 3. 定时任务

- 每日凌晨2点自动执行状态更新
- 批量更新过期和即将过期的证书状态
- 提供手动触发接口: `POST /api/certificates/update-status`

### 4. 数据验证

- 使用Bean Validation进行参数校验
- 证书名称、域名、过期时间等必填字段验证
- 全局异常处理统一错误响应

### 5. API文档

- 集成Swagger 3.0自动生成API文档
- 访问地址: `http://localhost:8080/swagger-ui/index.html`
- 完整的接口描述和参数说明

## 数据库设计

### certificates表结构

```sql
CREATE TABLE certificates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT '证书名称',
    domain VARCHAR(255) NOT NULL COMMENT '域名',
    issuer VARCHAR(255) COMMENT '颁发机构',
    issue_date DATETIME COMMENT '颁发日期',
    expiry_date DATETIME NOT NULL COMMENT '过期日期',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，2-即将过期，3-已过期',
    description TEXT COMMENT '描述',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_domain (domain),
    INDEX idx_expiry_date (expiry_date),
    INDEX idx_status (status)
);
```

## 配置说明

### application.yml核心配置

```yaml
server:
  port: 8080

spring:
  application:
    name: cert-management
  datasource:
    url: jdbc:mysql://localhost:3306/cert_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

cert:
  expiring-soon-days: 30

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

## 编译和运行

### 编译项目
```bash
mvn clean compile
```

### 运行项目
```bash
mvn spring-boot:run
```

### 访问应用
- 应用地址: http://localhost:8080
- API文档: http://localhost:8080/swagger-ui/index.html
- API接口: http://localhost:8080/v3/api-docs

## API接口示例

### 创建证书
```bash
curl -X POST http://localhost:8080/api/certificates \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试证书",
    "domain": "example.com",
    "issuer": "Let'\''s Encrypt",
    "issueDate": "2024-01-01 00:00:00",
    "expiryDate": "2024-12-31 23:59:59",
    "description": "测试用证书"
  }'
```

### 查询证书列表
```bash
curl -X GET "http://localhost:8080/api/certificates?page=1&size=10"
```

### 更新证书状态
```bash
curl -X POST http://localhost:8080/api/certificates/update-status
```

## 开发特性

1. **DDD分层架构**: 清晰的领域边界和职责分离
2. **统一响应格式**: 标准化的API响应结构
3. **全局异常处理**: 统一的错误处理机制
4. **参数验证**: 完整的输入验证
5. **自动状态管理**: 智能的证书状态计算
6. **定时任务**: 自动化的状态更新
7. **API文档**: 自动生成的接口文档

## 扩展建议

1. **用户认证**: 添加JWT或Session认证
2. **权限控制**: 实现基于角色的访问控制
3. **审计日志**: 记录操作历史
4. **通知功能**: 证书过期提醒
5. **批量操作**: 支持批量导入/导出
6. **监控告警**: 集成监控和告警系统

## 版本信息

- **版本**: 1.0.0-SNAPSHOT
- **构建状态**: ✅ 编译成功
- **测试状态**: 待完善
- **部署状态**: 本地开发环境就绪

---

*本文档生成时间: 2024年*