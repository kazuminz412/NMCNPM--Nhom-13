package com.bluemoon.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("API Tài Liệu - Quản Lý Chung Cư Blue Moon")
                                                .version("1.0.0")
                                                .description("Đây là tài liệu hướng dẫn gọi API cho dự án Đồ án Công nghệ Phần mềm - Nhóm 13. Dành cho Frontend và Tester sử dụng.")
                                                .contact(new Contact()
                                                                .name("Nhóm 13 - Backend Dev")
                                                                .email("nhom13@university.edu.vn"))
                                                .license(new License().name("Apache 2.0").url("http://springdoc.org")));
        }
}
