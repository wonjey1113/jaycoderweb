package com.jaycoder.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // 데이터의 변경을 감지
public class JayCoderWebApplication {

		public static void main(String[] args) {
			SpringApplication.run(JayCoderWebApplication.class, args);
		}
	

	
}
