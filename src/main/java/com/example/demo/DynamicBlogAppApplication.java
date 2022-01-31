package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.demo.dao.UserDao;
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserDao.class)
public class DynamicBlogAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicBlogAppApplication.class, args);
	}
} 	
