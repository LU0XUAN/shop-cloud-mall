# Shop-Cloud 商城微服务系统

基于 Spring Cloud Alibaba 的微服务架构商城系统，包含商品管理、订单管理、用户管理、文件服务等核心模块，配套管理后台与用户端前端。

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 开发语言 |
| Spring Boot | 2.7.3 | 基础框架 |
| Spring Cloud | 2021.0.3 | 微服务框架 |
| Spring Cloud Alibaba | 2021.0.1.0 | 微服务组件 |
| Nacos | 2.0.4 | 服务注册与配置中心 |
| Seata | 1.5.1 | 分布式事务 |
| MyBatis-Plus | 3.5.1 | ORM 框架 |
| MySQL | 8.0 | 关系型数据库 |
| Redis | - | 缓存 |
| Druid | 1.2.14 | 数据库连接池 |
| JWT | 0.9.1 | 身份认证 |
| Hutool | 5.8.1 | 工具类库 |
| Fastjson2 | 2.0.16 | JSON 处理 |
| Lombok | 1.18.42 | 代码简化 |
| Swagger | 3.0.0 | 接口文档 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.3.x | 前端框架 |
| Vite | 5.x | 构建工具 |
| Element Plus | 2.4.x | UI 组件库（管理后台） |
| Vant | 4.1.x | UI 组件库（移动端） |
| Pinia | 2.0.x | 状态管理 |
| Vue Router | 4.1.x | 路由管理 |
| Axios | 1.3.x | HTTP 请求 |
| ECharts | 5.4.x | 图表库 |
| Vue I18n | 9.13.x | 国际化 |

## 项目结构

```
shop-cloud/
├── shop-cloud/                  # 后端微服务
│   ├── shop-common/             # 公共模块
│   │   ├── shop-common-core/    # 核心工具类
│   │   ├── shop-common-redis/   # Redis 封装
│   │   └── shop-common-security/ # 安全认证
│   ├── shop-auth/               # 认证授权服务
│   ├── shop-gateway/            # API 网关（端口：8081）
│   ├── shop-api/                # 远程调用接口
│   │   ├── shop-api-user/       # 用户接口
│   │   ├── shop-api-goods/      # 商品接口
│   │   ├── shop-api-logs/       # 日志接口
│   │   └── shop-api-order/      # 订单接口
│   └── shop-modules/            # 业务模块
│       ├── shop-modules-user/   # 用户服务
│       ├── shop-modules-goods/  # 商品服务
│       ├── shop-modules-order/  # 订单服务
│       ├── shop-modules-file/   # 文件服务
│       └── shop-modules-logs/   # 日志服务
├── shop-admin/                  # 管理后台前端
└── shop-web/                    # 用户端前端
```

## 环境要求

- JDK 17+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+
- Redis
- Nacos 2.0+

## 快速开始

### 1. 启动基础服务

确保以下服务已启动并可访问：

- **Nacos**（默认端口：8848）
- **MySQL**（默认端口：3306）
- **Redis**（默认端口：6379）

### 2. 配置 Nacos

在 Nacos 控制台中创建命名空间，并在对应命名空间下添加各服务的配置文件。配置项参考各模块 `src/main/resources/` 下的 `application-dev.yml` 和 `bootstrap.yml`。

### 3. 启动后端服务

按以下顺序启动后端微服务：

```bash
# 进入后端根目录
cd shop-cloud

# 编译并安装公共模块到本地仓库
mvn clean install -pl shop-common -am

# 按顺序启动各服务
# 1. shop-auth      （认证服务）
# 2. shop-gateway   （网关服务）
# 3. shop-modules/* （各业务服务）
```

### 4. 启动前端

**管理后台（shop-admin）：**

```bash
cd shop-admin
npm install
npm run dev
```

**用户端（shop-web）：**

```bash
cd shop-web
npm install
npm run dev
```

## 服务端口

| 服务 | 端口 | 说明 |
|------|------|------|
| shop-gateway | 8081 | API 网关 |

> 各业务服务端口请参考 Nacos 中的配置。

## 核心功能

- **用户管理**：注册、登录、个人信息维护、收货地址管理
- **商品管理**：商品分类、商品信息、购物车
- **订单管理**：订单创建、订单查询、订单状态流转
- **文件服务**：文件上传与下载
- **日志服务**：系统操作日志记录
- **认证授权**：基于 JWT 的统一认证

## 许可证

本项目仅供学习交流使用。
