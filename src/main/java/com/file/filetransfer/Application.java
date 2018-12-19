package com.file.filetransfer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.file.filetransfer.dao")  //dao层
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
