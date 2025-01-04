## SpringDoc - OpenAPI 演示项目

### 简介
- 本项目大部分代码fork自 https://gitee.com/ant-laddie/hard-working-ant/tree/master/OpenAPI_3_Demo_Swagger3
- 致敬[蚂蚁小哥](https://gitee.com/ant-laddie)

本项目演示了如何在SpringBoot项目中，使用 SpringDoc 生成 OpenAPI 3.0 规范的接口文档
- Java版本：17；
- SpringBoot版本：3.4

### 项目演示地址
- Swagger : http://localhost:21002/swagger-ui/index.html
- Knife4j : http://localhost:21002/doc.html

### OpenAPI 规范简介
- OpenAPI规范（也称为 Swagger 3.x 规范） 是一种用于描述RESTful API的标准化格式；
- 它定义了如何描述API的基本信息、结构、参数、响应等方面的规范
- OpenAPI 文件允许您描述整个 API，包括：
  - 可用端点（ `/users` ）和每个端点上的操作（ `GET /users` ， `POST /users` ）
  - 每个操作的输入输出操作参数
  - 身份验证方法
  - 联系信息、许可证、使用条款和其他信息。

### SpringDoc 简介
- SpringDoc 是一个集成Swagger UI 和 ReDoc 的接口文档生成工具
- SpringDoc支持全新的OpenAPI 3.0规范，可以生成Swagger UI风格的接口文档

### 依赖项
- 项目是SpringMVC 框架时，使用SpirngDoc则引入以下依赖项：
```xml
  <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>${openapi.version}</version>
  </dependency>
```
- 如需要同时支持 Spring MVC 和 Spring WebFlux：则使用 `springdoc-openapi-starter-ui` 依赖项
- 如果 API 使用了安全机制（如 OAuth2 或 JWT），则需要引入单独的 `springdoc-openapi-security` 依赖项
- 当引入Mockserver时，其内置的swagger模块会和swagger3产生版本冲突，需在MockServer的依赖项中做如下排除：
```xml
<dependency>
    <groupId>org.mock-server</groupId>
    <artifactId>mockserver-netty</artifactId>
    <version>${mockserver.version}</version>
    <exclusions>
        <exclusion>
            <groupId>io.swagger.parser.v3</groupId>
            <artifactId>swagger-parser</artifactId>
        </exclusion>
        <exclusion>
            <groupId>io.swagger.parser.v3</groupId>
            <artifactId>swagger-models</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
### OpenAPI 的配置
- 参见 `OpenAPIConfig` 类；

### SpringDoc 的注解
- `@Tag` 用于对接口进行分类和归类，便于开发人员组织和管理 API 文档
- `@Operation` 用于描述 API 接口的操作，包括接口的描述、请求参数、响应参数等信息
- `@Parameter` 用于描述接口的请求参数，包括参数的名称、类型、描述、是否必填等信息
- `@Schema` 用于描述数据模型的基本信息和属性
- `@SecurityScheme`用于为API定义多种安全方案

### Knife4j的使用
- Knife4j 是基于 Swagger UI 的增强版本，专为中国开发者社区设计，主要目的是改善Swagger-UI比较丑陋的前端页面。
- 它在 springdoc-openapi 和 Swagger 3 的基础上进行了功能扩展，提供了更强大、更易用的 API 文档管理和展示工具
- 底层框架使用springdoc-openapi框架而非springfox时，需要使用Knife4j提供的对应3.x版本
- 需要注意的是该版本没有Knife4j提供的部分增强功能，是一个纯Ui，引入如下依赖即可：
```xml
  <dependency>
      <groupId>com.github.xiaoymin</groupId>
      <artifactId>knife4j-springdoc-ui</artifactId>
      <version>3.0.3</version>
  </dependency>
```
- 添加配置
```yaml
knife4j:
  enable: true
  setting:
    language: zh-CN
```

### Redoc的使用
- Redoc 是一个开源前端项目，支持 OpenAPI 3.0 规范，可以生成美观、易读的 API 文档页面；
- 添加RedocController，增加/redoc接口，重定向到/redoc.html文件；
- 在resource/static下，创建redoc.html文件；
- 暂时未调通，页面为空