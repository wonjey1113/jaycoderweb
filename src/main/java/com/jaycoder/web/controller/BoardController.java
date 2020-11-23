package com.jaycoder.web.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.BoardRepository;
import com.jaycoder.web.repository.UserRepository;
import com.jaycoder.web.service.BoardService;
import com.jaycoder.web.validator.BoardValidator;

@Controller
@RequestMapping("/board")
public class BoardController  {
	
		@Autowired
		private BoardRepository boardRespository;
	
		@Autowired
		private BoardValidator boardValidator;
		
		@Autowired
		private BoardService boardService;
		
		@Autowired
		private UserRepository userRepository;
		
		@GetMapping("/list")
		public String list(Model model, @PageableDefault(size = 5) Pageable pageable, 
				@RequestParam(required = false, defaultValue = "") String searchText) {
			//  boards.getPageable().getPageNumber(); // 현재 페이지 번호 
			// 	Page<Board> boards = boardRespository.findAll(pageable);
			//		Page<Board> boards = boardRespository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
				Page<Board> boards = boardRespository.findByTitleContainingOrContentContainingOrderByCreatedateDesc(searchText, searchText, pageable);
				int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
				int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
				model.addAttribute("startPage", startPage);
				model.addAttribute("endPage", endPage);
			 	model.addAttribute("boards", boards);
				return "board/list";
		}
		
		@GetMapping("/form")
		public String form(Model model, @RequestParam(required = false) Long id) {
			//System.out.println("GET mappong ");
				if(id == null) {
						model.addAttribute("board", new Board());	
				}else {					
						Board board = boardRespository.findById(id).orElse(null);
						if(!pagePermission(id, board)) {
								return "board/401";
						}				  
						model.addAttribute("board", board);
				}				
				return "board/form";
		}
		
		private boolean pagePermission(Long id, Board board) {
			 	Object principal  =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 	UserDetails userDetails = (UserDetails) principal;					 	
			 	User principalUser = userRepository.findByUsername(userDetails.getUsername());
			 	//principalUser.setUsername(userDetails.getUsername());
				//Board board = boardRespository.findById(id).orElse(null);
				if(!board.isSameWriter(principalUser)) {
						return false;
				}			
				return true;
		}
		
		@GetMapping("/{id}")
		public String view(@PathVariable Long id, Model model) {
				Board board = boardRespository.findById(id).orElse(null);
				model.addAttribute("board", board);				
				return "board/view";
		}
		
		@PostMapping("/form")
	  public String save(@Valid Board board, BindingResult bindingResult, 
	  		Authentication authentication) {
			  boardValidator.validate(board, bindingResult);
				if (bindingResult.hasErrors()) {
						return "board/form";
				}			
				String username = authentication.getName();
				boardService.save(username,board);				

		    return "redirect:/board/list";
	  }
		
		@DeleteMapping("/form/{id}")
		public String delete(@PathVariable Long id) {
				Board board = boardRespository.findById(id).orElse(null);
				if(!pagePermission(id, board)) {
						return "board/401";
				}			 	
			  boardRespository.deleteById(id);
			 	return "redirect:/board/list";
		}
		
}


