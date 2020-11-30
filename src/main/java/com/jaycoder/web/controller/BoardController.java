package com.jaycoder.web.controller;

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
		
		/**
		 * 게시글 목록
		 *
		 * @param Model model, Pageable pageable,  String searchField, String searchText
		 * @return view page
		 * @exception none
		 */		
		@GetMapping("/list")
		public String list(Model model, @PageableDefault Pageable pageable, 
				@RequestParam(required = false, defaultValue = "") String searchField,  @RequestParam(required = false, defaultValue = "") String searchText) {
			//  boards.getPageable().getPageNumber(); // 현재 페이지 번호 
			// 	Page<Board> boards = boardRespository.findAll(pageable);
			//		Page<Board> boards = boardRespository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
//			  Page<Board> boards = null;
//				int pageNumber = 0;
//				int pageTotal = 0;
//				if(searchField.equals("title")) {
//						System.out.println("title search..");
//						boards = boardService.searchTitle(searchText,  pageable);
//						pageNumber = boards.getPageable().getPageNumber();
//						pageTotal = boards.getTotalPages();
//				}
//				else if(searchField.equals("content")) {
//							System.out.println("content search..");
//							boards = boardService.searchContent(searchText,  pageable);
//				}
//				else if(searchField.equals("titleAndcontent")) {
//							System.out.println("title and content search..");
//							//boards = boardRespository.findByTitleContainingOrContentContainingOrderByCreatedateDesc(searchText, searchText, pageable);
//							boards = boardService.searchTitleAndContent(searchText, pageable);
//				}
//				else if(searchField.equals("writer")) {
//							System.out.println("writer search..");
////							User user = userRepository.findByUsername(searchText);
////							Long user_id = user.getId();
//							boards = boardService.searchWrite(searchText,  pageable);
//				}
//				else {
//							boards = boardService.searchTitle(searchText,  pageable);
//				}
			  Page<Board> boards = boardService.getBoardList(searchField, searchText, pageable);
				int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
				int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
				if(endPage == 0) endPage = 1;
//				System.out.println("startPage : "+startPage);
//				System.out.println("endPage : "+endPage);
//				int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
//				System.out.println("pageNumber : "+boards.getNumber());
//				System.out.println("page : "+page);

				model.addAttribute("startPage", startPage);
				model.addAttribute("endPage", endPage);
			 	model.addAttribute("boards", boards);
				return "board/list";
		}
		
		@GetMapping("/form")
		public String form(Model model, @RequestParam(required = false) Long id) {
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
//			 	Object principal  =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			 	UserDetails userDetails = (UserDetails) principal;
			 	String  getname = SecurityContextHolder.getContext().getAuthentication().getName();	 						 				  
			 	User principalUser = userRepository.findByUsername(getname);			
				if(!board.isSameWriter(principalUser)) {
						return false;
				}			
				return true;
		}
		
		/**
		 * 게시글 내용보기
		 *
		 * @param Long id, Model model, HttpServletResponse response, HttpServletRequest request
		 * @return view page
		 * @exception none
		 */
		@GetMapping("/{id}")
		public String view(@PathVariable Long id, Model model, HttpServletResponse response, HttpServletRequest request) {
				Board board = boardRespository.findById(id).orElse(null);
				if(board != null) {
					
						// 저장된 쿠키 불러오기 
						Cookie cookies[] = request.getCookies(); 
						Map<String, String> mapCookie = new HashMap<>(); 
						if(request.getCookies() != null){ 
								for (int i = 0; i < cookies.length; i++) { 
									Cookie obj = cookies[i]; 
									mapCookie.put(obj.getName(),obj.getValue());
								} 
					  } 
						// 저장된 쿠키중에 read_count 만 불러오기 
						String cookie_read_count = (String) mapCookie.get("read_count"); 
						// 저장될 새로운 쿠키값 생성 
						String new_cookie_read_count = "|" + id; 
						// 저장된 쿠키에 새로운 쿠키값이 존재하는 지 검사 
						if (StringUtils.indexOfIgnoreCase(cookie_read_count, new_cookie_read_count) == -1 ) { 
								// 없을 경우 쿠키 생성 
								Cookie cookie = new Cookie("read_count", cookie_read_count + new_cookie_read_count); 
								cookie.setMaxAge(1000); 	// 초단위 
								response.addCookie(cookie); 
								// 조회수 업데이트 this.bbsDao.updateReadCount(idx);
								boardService.updateReadCount(id);
						}					
				}
				model.addAttribute("board", board);				
				return "board/view";
		}
		
		@PostMapping("/form")
	  public String save(@Valid Board board, BindingResult bindingResult, 
	  		Authentication authentication) {
				System.out.println("content : "+board.getContent());
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


