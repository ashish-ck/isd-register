package com.opsmx.isd.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@ComponentScan(basePackages={"com.opsmx.isd.register"})
@EnableJpaRepositories(basePackages="com.opsmx.isd.register.repositories")
@EnableTransactionManagement
@EntityScan(basePackages="com.opsmx.isd.register")
@EnableScheduling
public class ISDRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ISDRegisterApplication.class, args);
    }
}
