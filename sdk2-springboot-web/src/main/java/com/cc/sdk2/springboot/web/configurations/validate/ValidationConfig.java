package com.cc.sdk2.springboot.web.configurations.validate;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 配置类验证
 * @author sen.hu
 * @date 2019/10/9 15:26
 **/
@Configuration
public class ValidationConfig {

    @Bean
    public Validator customizedValidator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                ;
        return validatorFactory.getValidator();
    }

    @Bean
    public MethodValidationPostProcessor createMethodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(customizedValidator());
        return methodValidationPostProcessor;
    }

}
