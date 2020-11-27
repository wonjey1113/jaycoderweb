package com.jaycoder.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jaycoder.web.model.User;
import com.jaycoder.web.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {
	
		@Autowired
		private UserService userService;
		
		@Autowired
		private AuthenticationManager authenticationManager;		
		
		@Autowired
		private PasswordEncoder passwordEncoder;
		
		
		@GetMapping("/login")
		public String login() {
				return "/account/login";
		}
		
		@GetMapping("/register")
		public String register() {
				return "/account/register";
		}		
		
		@PostMapping("/register")
		public String register(User user) {
				userService.save(user);
				return "redirect:/";
		}		
		
		@GetMapping("/update")
		public String updateForm(Authentication auth, Model model) {
				return "/account/update";
		}
		
		@PutMapping("/update")
		public String update(User user, HttpSession session) {
				userService.save(user);
			  List<GrantedAuthority> roles = new ArrayList<>(1);
			  roles.add(new SimpleGrantedAuthority("ROLE_USER"));
			  Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), roles);
			  SecurityContextHolder.getContext().setAuthentication(auth);								
				return "redirect:/";
		}
		
}
