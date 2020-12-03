package com.jaycoder.web.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
			
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
//			WebMvcConfigurer.super.addResourceHandlers(registry);
			  exposeDirectory("upload", registry);
		}
		
		private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
			// System.getProperty("user.dir")+"/upload"
      Path uploadDir = Paths.get(System.getProperty("user.dir")+"/"+dirName);
      String uploadPath = uploadDir.toFile().getAbsolutePath();
       
      if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
       
      registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
		}		

		@Bean(name = "filterMultipartResolver")
		public CommonsMultipartResolver multipartResolver() {
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
			multipartResolver.setDefaultEncoding("UTF-8"); // 파일 인코딩 설정
			multipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024); // 파일당 업로드 크기 제한 (5MB)
			return multipartResolver;
		}
		
		
}
