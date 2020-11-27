package com.jaycoder.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaycoder.web.model.Role;
import com.jaycoder.web.model.RoleType;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.UserRepository;

@Service
public class UserService {
	
		@Autowired
		private UserRepository userRepository;
		
		@Autowired
		private PasswordEncoder passwordEncoder;
		

	
		@Transactional
		public User save(User user) {		
				String encodedPassword = passwordEncoder.encode(user.getPassword()); 
				user.setPassword(encodedPassword);
				user.setEnabled(true);
				Role role = new Role();
				role.setId(1l);
				//user.getRoles().add(role);
				user.setRole(RoleType.USER);						
				return userRepository.save(user);
		}

		public User getPrincipalID(String username) {			
				return userRepository.findByUsername(username);
		}
}
