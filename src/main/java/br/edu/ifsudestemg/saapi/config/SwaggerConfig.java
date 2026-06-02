package br.edu.ifsudestemg.saapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.spi.service.contexts.SecurityContext;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableSwagger2
public class  SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("br.edu.ifsudestemg.saapi.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SAA API")
                .description("API do Sistema de Academia")
                .version("1.0")
                .contact(contact())
                .build();
    }

    private Contact contact() {
        return new Contact("Lucas do Carmo Silva" +
                "Rafaela Freitas Gomes" +
                "Sabrina Roriz"
                , "https://github.com/rafaelafgomes/saapi",
                "contato@dev-lucas.com" +
                        "rafaelafgomes@yahoo.com.br" +
                            "sabrinardroriz@gmail.com");
    }
}
