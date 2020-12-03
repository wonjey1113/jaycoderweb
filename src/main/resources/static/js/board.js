$(document).ready(function(){
	// 파일첨부 온체인지 함수 바인딩
	$(document).on('change','#upload-files' , function(){ 
		//uploadFile();
		// console.log($(this));
		// console.log(this.files.length);
		let maxSize  = 5 * 1024 * 1024;  // 5mb 
		let fileSize = 0;
		let fileCount = "";
		// Check if any file is selected. 
		if (this.files.length > 0) { 
			for (let i = 0; i <= this.files.length - 1; i++) { 

				let fsize = this.files.item(i).size; 
				fileSize += fsize;

			} 

			fileCount = this.files.length+"개 파일이 선택 되었습니다. "

			console.log(`total size : ${fileSize}`);
			if(fileSize > maxSize){
				console.log("Try to upload file less than 5MB!");
				$(".note-status-output").html(
					'<div class="alert alert-danger">' +
					'5MB 미만의 파일을 업로드하세요!' +
					'</div>'          
				);
				$(this).val("");
				$(this).siblings(".custom-file-label").addClass("selected").html("파일을 선택해 주세요.");
			}
			else{
				console.log(`${fileSize}bytes`);
				$(".note-status-output").html('');
				let selectedFilesInfo = fileCount + " ("+ getfileSize(fileSize);
				$(this).siblings(".custom-file-label").addClass("selected").html(selectedFilesInfo);				
			}
			console.log("pass size : 5242880");
		} 

		

		// if (this.files[0].size > 5242880) { 
		// 	alert("Try to upload file less than 5MB!"); 
		// } else { 
		// 	$('#GFG_DOWN').text(this.files[0].size + "bytes"); 
		// } 		
	 });
});

/* 파일 용량 표시 */
function getfileSize(x) {
	var s = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'];
	var e = Math.floor(Math.log(x) / Math.log(1024));
	return (x / Math.pow(1024, e)).toFixed(2) + ' ' + s[e];
  }

function insertImage(img, dir){
	console.log("/upload/"+ dir + "/" + img);
	let image_url = "/upload/"+ dir + "/" + img;
	$('.summernote').summernote("insertImage", image_url);


}