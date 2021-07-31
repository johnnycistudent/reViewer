package com.reviewer;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableEncryptableProperties
public class ReViewerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReViewerApplication.class, args);
	}

}
