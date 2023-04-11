package com.sang.nv.education.exam.web;

import com.sang.commonweb.config.AbstractSwaggerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;

@Configuration
@ConditionalOnExpression(value = "${spring.swagger.enabled:false}")
public class SwaggerConfig extends AbstractSwaggerConfig {

    @Override
    protected ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Todo Identity & Access Management Service")
                .description("Todo Exam service provides REST APIs to manage" +
                        " access management resources")
                .version("0.0.1")
                .build();
    }
}
