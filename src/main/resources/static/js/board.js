$(document).ready(function(){



	// 파일첨부 온체인지 함수 바인딩
	$(document).on('change','#upload-files' , function(){ 

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

	 });

	 // code...

});

$('.summernote').summernote({
	placeholder: '내용을 입력해 주세요.',
	lang: 'ko-KR',
	tabsize: 2,
	height: 400,
	callbacks:{
		onImageUpload: function(files, editor, welEditable) {
			console.log("onImageUpload run");
			console.log(`length : ${files.length}`);
			for (var i = files.length - 1; i >= 0; i--) {
				sendFile(files[i], this);
			}
		},
		onPaste: function (e) {
			var clipboardData = e.originalEvent.clipboardData;
			if (clipboardData && clipboardData.items && clipboardData.items.length) {
				var item = clipboardData.items[0];
				if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
					e.preventDefault();
				}
			}
		}			
	}              
});	

function sendFile(file, el) {

	let token = $("meta[name='_csrf']").attr("content");
	let header = $("meta[name='_csrf_header']").attr("content"); 
	
	var form_data = new FormData();
	  form_data.append('file', file);
	  $.ajax({
		beforeSend: function(xhr){
			xhr.setRequestHeader(header, token);
		},		  
		data: form_data,
		type: "POST",
		url: '/board/uploadSummernoteImageFile',
		cache: false,
		contentType: false,
		enctype: 'multipart/form-data',
		processData: false,
		success: function(data) {
			//  let uploadImages = $("#uploadImages").val();
			//  let fileSize = getfileSize(data.uploadSize);
			//  uploadImages += `<ul class="list-group  list-group-horizontal-sm  files-list">
			//  <li class="list-group-item">${data.uploadOImage}</li>
			//  <li class="list-group-item"><a href="#" class="link-file-add text-info" onclick="insertImage('${data.uploadSImage}','summernote_image')">본문에 추가</a></li>			 
			//  <li class="list-group-item"><a href="/api/upload/delete?=${data.uploadSImage}" class="link-file-del text-danger">삭제</a></li></ul>\n`;
			//  $("#uploadImages").val(uploadImages);		
			  $("#summernote_id").val(100);	 
			  $(el).summernote('editor.insertImage', data.url);
		}
	  });
}

/* 파일 용량 표시 */
function getfileSize(x) {
	var s = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'];
	var e = Math.floor(Math.log(x) / Math.log(1024));
	return (x / Math.pow(1024, e)).toFixed(2) + ' ' + s[e];
  }

  /* summernote insert image url  */
function insertImage(img, dir){
	console.log("/upload/"+ dir + "/" + img);
	let image_url = "/upload/"+ dir + "/" + img;
	$('.summernote').summernote("insertImage", image_url);
}

/* 첨부파일 삭제 */
$(".link-file-del").on("click", function(e) {
	e.preventDefault();
	if(confirm("[주의] 첨부된 파일이 삭제됩니다.\n\n정말로 삭제 하시겠습니까?")){
		let _this = $(this);
		onFileDelete(_this);
	}
  });

  function onFileDelete(_this){ 

	var deleteFile = _this;
	let url = deleteFile.attr("href");
	console.log(`delete url : ${url}`);
	
	let token = $("meta[name='_csrf']").attr("content");
	let header = $("meta[name='_csrf_header']").attr("content"); 
	
	$.ajax({
		beforeSend: function(xhr){
			xhr.setRequestHeader(header, token);
		},
		type : "delete",
		url : url,
		dataType : "json"
	}).fail(function(xhr, status) {
		console.log("error");   
	}).done(function(data, status) {

		if(data.valid){
			deleteFile.closest(".files-list").remove();
		}else{
			alert(data.errorMessage);
		}

	});  
  }

  function registerBoard(form) {
	
	let persent = getRandomArbitrary(50,100);
	$('.progress').css('width', persent+'%').attr('aria-valuenow', persent);
	$(".progress").removeClass("d-none");

	form.secret_yn.value = form.secret_yn.checked == false ? 'N' : 'Y';
	if( typeof form.notice_yn === 'object'){
		form.notice_yn.value = form.notice_yn.checked == false ? 'N' : 'Y';
	}

}

function getRandomArbitrary(min, max) {
	return Math.random() * (max - min) + min;
}

