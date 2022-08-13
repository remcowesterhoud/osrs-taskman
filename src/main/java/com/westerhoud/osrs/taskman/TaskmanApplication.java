package com.westerhoud.osrs.taskman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TaskmanApplication {

	@RequestMapping("/")
	@ResponseBody
	String helloWorld() {
		return "Hello, world!";
	}

	public static void main(String[] args) {
		SpringApplication.run(TaskmanApplication.class, args);
	}

}
