package com.ipayafrica.elipapower;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.ipayafrica.elipapower")
@SpringBootApplication(scanBasePackages={"com.ipayafrica.elipapower"})// same as @Configuration @EnableAutoConfiguration @ComponentScan

public class ElipaAPI extends SpringBootServletInitializer{

	public ElipaAPI() {
		// TODO Auto-generated constructor stub
	}
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ElipaAPI.class);
    }
     

	public static void main (String[] args){
        SpringApplication.run(ElipaAPI.class, args);
	}
}

 
