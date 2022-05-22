package com.jiashn.oneofficesso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author jiangjs
 * @date 2022-05-19 21:45
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.description}")
    private String description;

    @Value("${swagger.title}")
    private String title;
    @Value("${swagger.version}")
    private String version;
    @Value("${swagger.base-package}")
    private String basePackage;

    @Bean
    public Docket createSwaggerApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description(description)
                        .title(title)
                        .version(version)
                        .contact(new Contact("jiashn", "", "421582841@qq.com"))
                        .build()
                )
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }
}
