package com.lue.rasp.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author zhchen
 * Swagger3API文档的配置
 * knife4j文档地址： http://localhost:7081/doc.html#
 * swagger文档地址： http://localhost:7081/swagger-ui/index.html#/
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
public class Swagger3Config {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .build();
    }


    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SwaggerUI接口文档")
                .description("接口文档Swagger-Bootstrap版")
                .termsOfServiceUrl("http://localhost:7081/swagger-ui/index.html#/")
                .contact(new Contact("1ue","http://localhost:7081/doc.html#", "2336485988@qq.com"))
                .version("1.0")
                .license("1ue1uekin8")
                .build();
    }
}