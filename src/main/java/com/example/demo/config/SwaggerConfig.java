package com.example.demo.config;

import com.example.demo.controller.HelloController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //RequestHandlerSelectors:配置要扫描的方式
                //any()：扫面全部
                //none():不扫描
                //basepackage():基于包的扫描
                //withClassAnnotation():扫描类上的注解，参数是一个注解的参数
                //withMethodAnnotation():扫描方法上的注解下
                .apis(RequestHandlerSelectors.withMethodAnnotation(RequestMapping.class))
                //过滤某个路径下不扫描
                .paths(PathSelectors.ant("/hello/**"))
                .build();
    }

    private ApiInfo apiInfo(){
        Contact contact = new Contact("maying","/localhost","1191832613@qq.com");
        return new ApiInfo(
                "MaYing Demo",
                "Api Documentation",
                "v1.0", "urn:tos",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                 new ArrayList<VendorExtension>());
    }
}
