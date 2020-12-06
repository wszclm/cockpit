package com.cockpit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * 羿盾驾驶舱
 */

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableSwagger2
@ServletComponentScan
@MapperScan(basePackages="com.cockpit.dao")
@EnableAutoConfiguration(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class CockpitApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		return application.sources(CockpitApplication.class);
	}

	public static void main(String[] args) {
		try {
			SpringApplication.run(CockpitApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
