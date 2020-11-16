package com.jaycoder.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jaycoder.web.repository.BoardRespository;

@Controller
@RequestMapping("/board")
public class BoardController {
	
		@Autowired
		private BoardRespository boardRespository;
	
		@GetMapping("/list")
		public String list(Model model) {
			
			 	model.addAttribute("boards", boardRespository.findAll());
				return "board/list";
		}
}
