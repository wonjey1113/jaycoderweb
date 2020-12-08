package com.jaycoder.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.jaycoder.web.model.Attach;
import com.jaycoder.web.model.Board;
import com.jaycoder.web.model.Summernote;
import com.jaycoder.web.model.User;
import com.jaycoder.web.repository.BoardRepository;
import com.jaycoder.web.repository.UserRepository;
import com.jaycoder.web.service.AttachService;
import com.jaycoder.web.service.BoardService;
import com.jaycoder.web.util.FileUtils;
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
		
		@Autowired
		private AttachService attachService;
		
		@Autowired
	 ResourceLoader resourceLoader;		
		
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

			  Page<Board> boards = boardService.getBoardList(searchField, searchText, pageable);
				int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
				int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
				if(endPage == 0) endPage = 1;

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
						List<Summernote> imageUpload = attachService.summernoteFindByid(id);
						model.addAttribute("imageUpload", imageUpload);						
				}				
				return "board/form";
		}
		
		/**/
		@GetMapping("/attach/{id}/download")
		private void fileDown(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception{
      
      request.setCharacterEncoding("UTF-8");
      Attach fileVO = attachService.fileDetailService(id);
      
      //파일 업로드된 경로 
      try{
		  		String today = attachService.getModifydate(id);
				  String uploadPath = System.getProperty("user.dir") + "/upload/"+today;
				  String uploadFile = attachService.getFilename(id);

          String fileUrl = uploadPath;
          fileUrl += "/";
          String savePath = fileUrl;
          String fileName = uploadFile;
          
          //실제 내보낼 파일명 
          String oriFileName = fileVO.getOriginal_name();
          InputStream in = null;
          OutputStream os = null;
          File file = null;
          boolean skip = false;
          String client = "";
          
          //파일을 읽어 스트림에 담기  
          try{
              file = new File(savePath, fileName);
              in = new FileInputStream(file);
          } catch (FileNotFoundException fe) {
              skip = true;
          }
          
          client = request.getHeader("User-Agent");
          
          //파일 다운로드 헤더 지정 
          response.reset();
          response.setContentType("application/octet-stream");
          response.setHeader("Content-Description", "JSP Generated Data");
          
          if (!skip) {
              // IE
              if (client.indexOf("MSIE") != -1) {
                  response.setHeader("Content-Disposition", "attachment; filename=\""
                          + java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
                  // IE 11 이상.
              } else if (client.indexOf("Trident") != -1) {
                  response.setHeader("Content-Disposition", "attachment; filename=\""
                          + java.net.URLEncoder.encode(oriFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
              } else {
                  // 한글 파일명 처리
                  response.setHeader("Content-Disposition",
                          "attachment; filename=\"" + new String(oriFileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
                  response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
              }
              response.setHeader("Content-Length", "" + file.length());
              os = response.getOutputStream();
              byte b[] = new byte[(int) file.length()];
              int leng = 0;
              while ((leng = in.read(b)) > 0) {
                  os.write(b, 0, leng);
              }
          } else {
              response.setContentType("text/html;charset=UTF-8");
              System.out.println("<script language='javascript'>alert('파일을 찾을 수 없습니다');history.back();</script>");
          }
          in.close();
          os.close();
      } catch (Exception e) {
          System.out.println("ERROR : " + e.getMessage());
      }
      
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
		@GetMapping("/{id}/details")
		public String view(@PathVariable Long id,  @RequestParam(value = "page") String page,  
				@RequestParam(value = "searchField") String searchField,  
				@RequestParam(value = "searchText") String searchText, Model model, 
				HttpServletResponse response, HttpServletRequest request) {
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
				model.addAttribute("page", page);
				model.addAttribute("searchField", searchField);
				model.addAttribute("searchText", searchText);
				
				
				return "board/view";
		}
		
		// @RequestParam("file") MultipartFile file
		@PostMapping("/form")
	  public String save(@Valid Board board,  BindingResult bindingResult, 
	  		Authentication authentication, MultipartFile[] uploadfile) {

				if(board.getNotice_yn() == null) {
					board.setNotice_yn("N");
				}
				if(board.getSecret_yn() == null) {
					board.setSecret_yn("N");
				}
				System.out.println("search_yn : "+board.getSecret_yn());
							
				@SuppressWarnings("unused")
				int attach_rows = 0;
				System.out.println("content : "+board.getContent());
			  boardValidator.validate(board, bindingResult);
				if (bindingResult.hasErrors()) {
						return "board/form";
				}
				
				String username = authentication.getName();
				board = boardService.save(username,board);				

				// 파일 업로드 처리 로직
				// uploadPath : C:\Users\wonje\AppData\Local\Temp\tomcat-docbase.8090.3773065005175977297
				// String uploadPath = request.getSession().getServletContext().getRealPath("/upload/");
				//System.out.println("uploadPath : " + uploadPath);
				List<Attach> fileList = FileUtils.uploadFiles(uploadfile, board.getId());
				if (CollectionUtils.isEmpty(fileList) == false) {
						attach_rows = attachService.insertAttach(fileList);				
				}				
				
				// 썸머노트 이미지 업로드 문자열 처리
				if(!board.getSummernote_id().equals(null)) {
					 attachService.updateSummernoteid(board.getId());
				}
				
		    return "redirect:/board/list";
	  }
		
		@DeleteMapping("/form/{id}")
		public String delete(@PathVariable Long id) {
				Board board = boardRespository.findById(id).orElse(null);
				if(!pagePermission(id, board)) {
						return "board/401";
				}			 	
				// 첨부파일 삭제
				attachService.deleteFiles(id);
				
			  boardRespository.deleteById(id);
			 	return "redirect:/board/list";
		}
		
		@PostMapping(value="/uploadSummernoteImageFile", produces = "application/json")
		@ResponseBody
		public JsonObject uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {
			
			JsonObject jsonObject = new JsonObject();
			//System.out.println("uplaodSummernoteImageFile run");
			// String uploadPath = System.getProperty("user.dir") + "/upload/"+today;
//			LocalDate currentDate = LocalDate.now();
//			String nowDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
					
			String fileRoot = System.getProperty("user.dir") + "/upload/summernote_image/";	//저장될 외부 파일 경로
			System.out.println("fileRoot : "+fileRoot);
			String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
			Long size = multipartFile.getSize();
			String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
					
			String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명
			
			File targetFile = new File(fileRoot + savedFileName);	
			
			try {
				InputStream fileStream = multipartFile.getInputStream();
				org.apache.commons.io.FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
				jsonObject.addProperty("url", "/upload/summernote_image/"+savedFileName);
//				jsonObject.addProperty("uploadOImage", originalFileName);
//				jsonObject.addProperty("uploadSImage", savedFileName);
//				jsonObject.addProperty("uploadSize", size);
//				jsonObject.addProperty("uploadDate", nowDate);
				jsonObject.addProperty("responseCode", "success");
				
				/* 파일 정보 저장 */
				Summernote attach = new Summernote();
				attach.setBoard_idx(0l);
				attach.setOriginal_name(originalFileName);
				attach.setSave_name(savedFileName);
				attach.setSize(size);				
				attachService.insertImage(attach);
					
			} catch (IOException e) {
				 org.apache.commons.io.FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
				jsonObject.addProperty("responseCode", "error");
				e.printStackTrace();
			}
			
			return jsonObject;
		}		
		
		
}


