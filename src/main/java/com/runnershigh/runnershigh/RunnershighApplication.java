package com.runnershigh.runnershigh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.runnershigh.runnershigh.mapper")
public class RunnershighApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunnershighApplication.class, args);
	}

}
