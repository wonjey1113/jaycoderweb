package com.jaycoder.web.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.jaycoder.web.exception.AttachFileException;
import com.jaycoder.web.model.Attach;
import com.jaycoder.web.model.Board;

@Component
public class FileUtils {
	
	//	static ServletWebRequest servletContainer = (ServletWebRequest)RequestContextHolder.getRequestAttributes();
	//	static HttpServletRequest request = servletContainer.getRequest();
	//	//HttpServletResponse response = servletContainer.getResponse();
		@Autowired
		static
		HttpServletRequest request;
	
		/** 오늘 날짜 */
		private final static String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
	
		/** 업로드 경로 */
		//private final String uploadPath = Paths.get("C:", "develop", "upload", today).toString();
	  private final static String uploadPath = System.getProperty("user.dir") + "/upload/"+today;
	 	// System.getProperty("user.dir")
    
	  /** 기본 업로드 경로  */
	  private final static String rootUploadPath = System.getProperty("user.dir") + "/upload"; 
	
		/**
		 * 서버에 생성할 파일명을 처리할 랜덤 문자열 반환
		 * @return 랜덤 문자열
		 */
		private final static String getRandomString() {
			return UUID.randomUUID().toString().replaceAll("-", "");
		}
	
		/**
		 * 서버에 첨부 파일을 생성하고, 업로드 파일 목록 반환
		 * @param files    - 파일 Array
		 * @param boardIdx - 게시글 번호
		 * @return 업로드 파일 목록
		 */
		public static List<Attach> uploadFiles(MultipartFile[] files, Long boardIdx) {
	
			//System.out.println("system getproperty : "+System.getProperty("user.dir"));
			//String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
			//String uploadPath = path+ today;
	//		String uploadPath = path;
			// G:\JAVA\eclipse09-workspace\jayCoderWeb/upload/
		  Path path = Paths.get(System.getProperty("user.dir")+"/upload/"+today);
		  String uploadPath1 = path.toFile().getAbsolutePath();
		  System.out.println("uplaod/** : "+uploadPath1);
			
			for (MultipartFile multipartFile : files) {
					System.out.println("--------------------------------------");
					System.out.println("Upload File Name : " + multipartFile.getOriginalFilename());
					System.out.println("Upload File Size : " + multipartFile.getSize());			
			}
			
			/* 파일이 비어있으면 비어있는 리스트 반환 */
//			System.out.println("files length : "+files.length);
//			System.out.println("files[0].getsize : "+files[1].getSize());
//			System.out.println("files[0].getOriginalFilename : "+files[1].getOriginalFilename());
			if (files[0].getSize() < 1) {
				System.out.println("---------------------파일없음---------------------");
				return Collections.emptyList();
			}
	
			/* 업로드 파일 정보를 담을 비어있는 리스트 */
			List<Attach> attachList = new ArrayList<>();
	
			/* uploadPath에 해당하는 디렉터리가 존재하지 않으면, 부모 디렉터리를 포함한 모든 디렉터리를 생성 */
			File dir = new File(uploadPath);
			System.out.println("uploadPath : "+uploadPath);
			if (dir.exists() == false) {
	
				dir.mkdirs();
				System.out.println("경로 생성 ");
			}
	
			/* 파일 개수만큼 forEach 실행 */
			for (MultipartFile file : files) {
				try {
					/* 파일 확장자 */
					final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
					/* 서버에 저장할 파일명 (랜덤 문자열 + 확장자) */
					final String saveName = getRandomString() + "." + extension;
	
					/* 업로드 경로에 saveName과 동일한 이름을 가진 파일 생성 */
					File target = new File(uploadPath, saveName);
					file.transferTo(target);
	
					/* 파일 정보 저장 */
					Attach attach = new Attach();
					Board board2 = new Board();
					board2.setId(boardIdx);
					//attach.setBoard_idx(boardIdx);
					attach.setBoard2(board2);
					attach.setOriginal_name(file.getOriginalFilename());
					attach.setSave_name(saveName);
					attach.setSize(file.getSize());
	
					/* 파일 정보 추가 */
					attachList.add(attach);
	
				} catch (IOException e) {
					throw new AttachFileException("[" + file.getOriginalFilename() + "] failed to save file...");
	
				} catch (Exception e) {
					throw new AttachFileException("[" + file.getOriginalFilename() + "] failed to save file...");
				}
			} // end of for
	
			return attachList;
		}
		
		public static String boardFileDelete(String fileName, String dateDir ) {
				//String dateDir =  modifydate.format(DateTimeFormatter.ofPattern("yyMMdd"));				
				String path = rootUploadPath + File.separator + dateDir + File.separator + fileName; 
				
				File file = new File(path);
				if(file.exists() == true) {
						file.delete();
				}
				
				return null;
		}


}
