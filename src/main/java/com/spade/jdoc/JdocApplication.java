package com.spade.jdoc;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class JdocApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdocApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components().addSecuritySchemes("basicScheme", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"))
//                        .addParameters("token", new Parameter().in("header").schema(new StringSchema())
//                                .name("token")).addHeaders("myHeader2", new Header().description("myHeader2 header").schema(new StringSchema())))
//                .info(new Info()
//                        .title("lock by login").version("1.0")
//                        .description("please use login api to get token.")
//                        );
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("token",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title("login check API").version("1.0").description("please use login api to get token.")
                );
    }
}
