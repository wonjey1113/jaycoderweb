package com.jaycoder.web.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaycoder.web.model.Reply;
import com.jaycoder.web.repository.ReplyRepository;

@Service
public class ReplyService {
		
//		@Autowired
//		private BoardRepository boardRepository;
//		
//		@Autowired(required = true)
//		private UserRepository userRepository;
		
		@Autowired
		private ReplyRepository replyRepository;
		
		@Transactional
		public Reply update(Long boardid, Long id, String content) {
			  Reply reply = replyRepository.findById(id).get();
			  reply.setContent(content);
				return reply;
		}
		
		
}
