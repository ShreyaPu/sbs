package com.alphax.config;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This is Configuration class for swagger.
 *
 * @author A106744104
 */
@Configuration
@Slf4j
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Value("${alphax.swagger.url}")
    private String swaggerUrl;
    
    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    /**
     * @return {@link Docket} Bean with controller classes configuration.
     */
    @Bean
    public Docket apiController() {
        HashSet<String> consumesAndProduces = new HashSet<>(Collections.singletonList("application/json"));
        HashSet<String> protocols = new HashSet<>(Collections.singletonList("http"));
        
        return getDocket(consumesAndProduces, protocols)
                .host(swaggerUrl)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.alphax.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private Docket getDocket(HashSet<String> consumesAndProduces, HashSet<String> protocols) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .consumes(consumesAndProduces)
                .produces(consumesAndProduces)
                .protocols(protocols)
                .ignoredParameterTypes(java.sql.Date.class)
                .ignoredParameterTypes(java.time.LocalDate.class)
                .directModelSubstitute(java.time.LocalDate.class, Date.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Lists.newArrayList(securityContext()))
                .useDefaultResponseMessages(false);
    }

    /**
     * @return API info Bean of {@link ApiInfo} type.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("ALPHAX REST API").description("ALPHAX API is RESTful services for Dealer management application.")
                .version("API ALPHAX 1.0")
                .termsOfServiceUrl("Terms of service")
                .contact(new Contact("Ajay Maheshwari", "", "ajay.maheshwari@t-systems.com"))
                .license("License of API").licenseUrl("http://www.t-systems.com")
                .build();
    }
    
	/**
	 * 
	 * @return ApiKey
	 */
	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}
	 
	private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
            .build();
    }
	
	List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        log.info("authorizationScopes[0] "+ authorizationScopes[0] );
        return Lists.newArrayList(
            new SecurityReference("JWT",  new AuthorizationScope[0]));
    }
	
}