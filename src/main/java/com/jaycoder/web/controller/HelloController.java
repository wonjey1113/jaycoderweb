package com.jaycoder.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	    // 메소드 않주면 기본은 Get
			@RequestMapping("/hello")
			public String hello() {
					return "Hello 스프링부트!!";
			}

}
