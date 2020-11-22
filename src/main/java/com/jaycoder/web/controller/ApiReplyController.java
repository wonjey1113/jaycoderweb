package com.jaycoder.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.Reply;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.BoardRepository;
import com.jaycoder.web.repository.ReplyRepository;
import com.jaycoder.web.repository.UserRepository;

@RestController  
@RequestMapping("/api/board/{boardId}/replys")
public class ApiReplyController {
	
		@Autowired
		private ReplyRepository replyRepository;
		
		@Autowired
		private UserRepository userRepository;
		
		@Autowired
		private BoardRepository boardRepository;
		
		@PostMapping("")
		public Reply create(@PathVariable Long boardId, String content, Authentication authentication) {
				String username = authentication.getName();
				User user = userRepository.findByUsername(username);
				Board board = boardRepository.findById(boardId).get(); 
				Reply  reply = new Reply(content, user, board);
				return replyRepository.save(reply);
		}

}