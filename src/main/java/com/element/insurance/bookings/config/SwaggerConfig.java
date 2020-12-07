package com.element.insurance.bookings.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Ajay Singh Pundir
 * Swagger Configuration
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket bookingTrailsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .pathMapping("")
                .apiInfo(getApiInfo());
    }

    //create api metadata that goes at the top of the generated page
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Booking Trails API")
                .version("1.0")
                .description("API for managing bookings.")
                .license("Apache License Version 2.0")
                .build();
    }

}
