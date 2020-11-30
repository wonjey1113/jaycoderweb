package com.jaycoder.web.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.BoardRepository;
import com.jaycoder.web.repository.UserRepository;

@Service
public class BoardService {
		
		@Autowired
		private BoardRepository boardRepository;
		
		@Autowired(required = true)
		private UserRepository userRepository;
		
		public Board save(String username, Board board) {
				User user = userRepository.findByUsername(username);
				board.setUser(user);
				return boardRepository.save(board);
		}

		/**
		 * 게시글 업데이트 
		 *
		 * @param Long id 게시글 id, Long 타입의 게시글 id
		 * @return none
		 * @exception none
		 */
		@Transactional
		public void updateReadCount(Long id) {
				Board board = boardRepository.findById(id).orElse(null);
				int hit = board.getHit() + 1;
				board.setHit(hit);
		}

		public Page<Board> searchTitle(String searchText, Pageable pageable) {
				
				return boardRepository.findByTitleContainingOrderByCreatedateDesc(searchText, pageable);
		}

		public Page<Board> searchContent(String searchText, Pageable pageable) {

				return boardRepository.findByContentContainingOrderByCreatedateDesc(searchText, pageable);
		}

		public Page<Board> searchWrite(String username, Pageable pageable) {

				return boardRepository.findByUsername(username, pageable);
		}

		public Page<Board> searchTitleAndContent(String searchText, Pageable pageable) {

			  return boardRepository.findByTitleContainingOrContentContainingOrderByCreatedateDesc(searchText, searchText, pageable);
		}
		
		public Page<Board> getBoardList(String searchField, String searchText ,Pageable pageable){
			
					int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
					System.out.println("page : "+page);
		      pageable = PageRequest.of(page, 10);			

					if(searchField.equals("title")) {
							System.out.println("title search..");
							return boardRepository.findByTitleContainingOrderByCreatedateDesc(searchText, pageable);
					}
					else if(searchField.equals("content")) {
								System.out.println("content search..");
								return boardRepository.findByContentContainingOrderByCreatedateDesc(searchText, pageable);
					}
					else if(searchField.equals("titleAndcontent")) {
								System.out.println("title and content search..");
								return boardRepository.findByTitleContainingOrContentContainingOrderByCreatedateDesc(searchText, searchText, pageable);
					}
					else if(searchField.equals("writer")) {
								System.out.println("writer search..");
								return boardRepository.findByUsername(searchText, pageable);
					}
					else {
							return boardRepository.findByTitleContainingOrderByCreatedateDesc(searchText, pageable);
					}			
			
		}
		
}
