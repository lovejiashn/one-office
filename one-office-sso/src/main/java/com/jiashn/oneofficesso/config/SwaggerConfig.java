package com.jiashn.oneofficesso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

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
                .build()
                //设置Authroize,即swagger调试程序时，设置token后，接口可以共用
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    /**
     * 设置请求头信息
     */
    private List<ApiKey> securitySchemes(){
        List<ApiKey> apiKeys = new ArrayList<>();
        //请求头名称name,keyname与配置文件设置的名称一致
        ApiKey apiKey = new ApiKey("Authorization","Authorization","Header");
        apiKeys.add(apiKey);
        return apiKeys;
    }

    /**
     * 设置哪些路径需要进行登录
     * @return
     */
    private List<SecurityContext> securityContexts(){
        List<SecurityContext> contexts = new ArrayList<>();
        contexts.add(getSecurityContexts(basePackage));
        return contexts;
    }

    private SecurityContext getSecurityContexts(String path) {
        return SecurityContext.builder()
                .securityReferences(getSecurityContexts())
                .forPaths(PathSelectors.regex(path))
                .build();
    }

    private List<SecurityReference> getSecurityContexts() {
        List<SecurityReference> references = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global","accessEverything");
        AuthorizationScope[] scopes = new AuthorizationScope[]{authorizationScope};
        references.add(new SecurityReference("Authorization",scopes));
        return references;
    }
}
