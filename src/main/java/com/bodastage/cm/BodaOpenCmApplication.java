package com.bodastage.cm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableJms
//@Configuration
//@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"com.bodastage.cm"})
@ComponentScan("com.bodastage.cm")
@SpringBootApplication
public class BodaOpenCmApplication {

	public static void main(String[] args) {
		SpringApplication.run(BodaOpenCmApplication.class, args);
	}
	
	/**
	 * Configure CORS
	 * 
	 * @return
	 */
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //registry.addMapping("/**");
                //registry.addMapping("/api").allowedOrigins("http://localhost:7000");
            	
                registry.addMapping("/**")
                .allowCredentials(false)
                //.allowedMethods("GET", "POST", "OPTIONS", "PUT")
                //.allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
                //        "Access-Control-Request-Headers")
                //.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
                .allowCredentials(true).maxAge(3600);
            }
        };
    }
}