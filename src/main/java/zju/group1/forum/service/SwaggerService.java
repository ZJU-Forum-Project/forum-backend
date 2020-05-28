package zju.group1.forum.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;

@Configuration
@EnableSwagger2
@ComponentScan("zju.group1.forum.controller")
@ComponentScan("zju.group1.forum.dto")
public class SwaggerService {
    @Configuration
    @EnableSwagger2
    public class SwaggerConfig {
        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.any())
                    .paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo());
        }

        private ApiInfo apiInfo() {
            return new ApiInfo(
                    "Forum交互文档",
                    "前端同学注意：凡是涉及登录的都需要传入参数'Authorization'," +
                            "接口中没有提及也需要传入，后端对应的注解为@AuthToken\n" +
                            "第一轮 前端：陈诺，彭子帆 后端：王钟毓，孔成俊\n" +
                            "第二轮 前端：王鹏，李林瀚 后端：陈思启，席吉华\n" +
                            "第三轮 前端：代艺博，刘乐为 后端：彭子帆，王汀\n" +
                            "第四轮 前端：王钟毓，孔成俊 后端：陈诺，李林瀚\n" +
                            "第五轮 前端：",
                    "V4.0.0",
                    "https://github.com/orgs/ZJU-Forum-Project/dashboard",
                    new Contact("", "", ""),
                    "", "", Collections.emptyList());
        }
    }
}
