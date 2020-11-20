package com.jaycoder.web.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

@RunWith(SpringRunner.class)
@WebMvcTest(HelloController.class)
class HelloControllerTest {

	@Autowired
	MockMvc mock;  // 가상의 request(요청) 를 만든다. 브라우저 없는
	
	@Test
	public void testHello() throws Exception{
			mock.perform(get("/hello"))
					.andExpect(status().isOk()).
					andExpect((ResultMatcher) content().string("Hello 스프링부트!!"));
			
			MvcResult result = (MvcResult) mock.perform(get("/hello"))
						.andExpect(status().isOk())
						.andReturn();
			
			assertEquals("Hello 스프링부트!!", result.getResponse().getContentAsString());
			assertThat(result.getResponse().getContentAsString()).isEqualTo("Hello 스프링부트!!");
			
			System.out.println("RRR>>" + result.getResponse().getContentAsString());
	}
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
