package com.westerhoud.osrs.taskman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TaskmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanApplication.class, args);
	}

}
