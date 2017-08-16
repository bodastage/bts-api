package com.bodastage.cm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
//@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class, basePackages = "com.bodastage.cm")
@EnableJpaRepositories(basePackages = "com.bodastage.cm")
@ComponentScan("com.bodastage.cm")
//@EntityScan("com.bodastage.cm")
@EnableAutoConfiguration
@SpringBootApplication
public class BodaOpenCmApplication {

	public static void main(String[] args) {
		SpringApplication.run(BodaOpenCmApplication.class, args);
	}
}