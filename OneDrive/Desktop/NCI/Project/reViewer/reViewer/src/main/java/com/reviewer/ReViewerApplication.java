package com.reviewer;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@Configuration
@EnableEncryptableProperties
@EnableJpaAuditing
public class ReViewerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReViewerApplication.class, args);
	}

}
