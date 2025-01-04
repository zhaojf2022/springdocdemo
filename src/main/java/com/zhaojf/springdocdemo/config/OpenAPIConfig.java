package com.zhaojf.springdocdemo.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        // 定义测试环境服务器URL
        String devUrl = "http://localhost:21002";
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("测试环境的服务器URL");

        // 定义生产环境服务器URL
        String prodUrl = "http://localhost:21002";
        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("正式环境的服务器URL");

        // 定义联系人对象
        Contact contact = new Contact();
        contact.setEmail("contact@example.com");
        contact.setName("FirtName LastName");
        contact.setUrl("https://www.example.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        // 定义API信息对象
        Info info = new Info()
            .title("Spring Doc 演示 API")
            .version("1.0")
            .contact(contact)
            .description("对外API接口说明.")
            .termsOfService("https://www.example.com/terms")
            .license(mitLicense);

        // 返回OpenAPI对象，包含API信息和服务器信息
        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));    
    }
    
}
