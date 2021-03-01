package com.cc.sdk2.springboot.web.configurations.validate;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
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
        ValidatorFactory validatorFactory = Validation
                .byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                ;
        return validatorFactory.getValidator();

//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setDefaultEncoding("UTF-8");
//        messageSource.setCacheMillis(-1);
////        messageSource.setBasename("ValidationMessages");
//
//        LocalValidatorFactoryBean validator = ValidationAutoConfiguration.defaultValidator();
//        validator.setValidationMessageSource(messageSource);
//        validator.setMessageInterpolator(new MessageInterpolatorFactory().getObject());
//        validator.setProviderClass(HibernateValidator.class);
//        return validator;
    }

    @Bean
    public MethodValidationPostProcessor createMethodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(customizedValidator());
        return methodValidationPostProcessor;
    }

}
