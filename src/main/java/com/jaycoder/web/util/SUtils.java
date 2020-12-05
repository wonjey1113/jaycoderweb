package com.jaycoder.web.util;

import org.springframework.stereotype.Component;

	@Component
	public interface SUtils {
		/**
		 * 파일 사이즈 포맷 변경
		 *
		 * @param String size
		 * @return byte, kb, mb, gb size
		 * @exception none
		 */		
		static String setFormatFileSize(String size) {
			int isize = Integer.parseInt(size) ;
			String[] s = {"bytes","kB","MB","GB","TB","PB"};
			double e = Math.floor(Math.log(isize) / Math.log(1024));	
			return String.format("%.2f", (isize / Math.pow(1024, e))) + s[(int) e];
		}
		
		static String getMimetype(String str) {
			String result = str.substring(str.length()-3, str.length());		
			String retVlue = "undefined";
			switch (result) {
			case "jpg":
			case "jpe":
			case "peg":
			case "png":
			case "gif":
			case "bmp":
				retVlue = "image";
				break;
			case "zip":
				retVlue = "zip";
				break;
			case "txt":
				retVlue = "text";
				break;
		  }
			return retVlue;
	}		
	
}
