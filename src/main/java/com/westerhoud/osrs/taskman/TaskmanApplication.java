package com.westerhoud.osrs.taskman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class TaskmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanApplication.class, args);
	}

}
