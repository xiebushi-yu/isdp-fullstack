package edu.scau.mis.web.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI配置
 * SwaggerV3配置类
 * 基本应用Controller无需进行任何配置
 */
@Configuration
public class OpenApiConfig {
    /**
     * 配置OpenAPI
     * 文档UI访问地址:
     * http://localhost:8081/swagger-ui/index.html
     *
     * @return OpenAPI配置对象
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ISDP项目接口文档")
                        .description("SpringBoot3项目集成OpenAPI接口文档")
                        .version("v1.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("项目API文档")
                        .url("/"));
    }
}
