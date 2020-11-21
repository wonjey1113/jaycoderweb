package com.jaycoder.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.Reply;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.ReplyRepository;
import com.jaycoder.web.repository.UserRepository;

@Controller  
@RequestMapping("/board/{boardId}/replys")
public class ReplyController {
	
		@Autowired
		private ReplyRepository replyRepository;
		
		@Autowired
		private UserRepository userRepository;
		
		@PostMapping("")
		public String create(@PathVariable Long boardId, String content, Authentication authentication) {
				String username = authentication.getName();
				User user = userRepository.findByUsername(username);	
				Board board = new Board();
				board.setId(boardId);
				Reply  reply = new Reply(content, user, board);
				replyRepository.save(reply);
				return String.format("redirect:/board/%d", boardId);
		}

}
