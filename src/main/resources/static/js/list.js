$(document).ready(function(){

	 $("#searchCancelBtn").on("click",function(){
		//  console.log("search cancel button clicked!");
		//  console.log($("#searchForm"));
		let form = $("#searchForm");
		form.find("input[name=searchText]").val('');
		form.find("select").val("title").prop("selected", true);
		form[0].submit();
	 });

	 $('[data-toggle="tooltip"]').tooltip();

	 $(".link-list-article").on("click", function(e){
		e.preventDefault();
//		let _this = $(this);  
//		onReplyUpdate(_this);
		// console.log("게시글 클릭");
		let url  = $(this).attr("href");
		console.log(`url : ${url}`);
		//console.log($("#page").text());
		let page = $("#page").text();
		let searchField = $("#searchField").text();
		let searchText = $("#searchText").text();
		let arr_url = url.split("/");
		// console.log(`length : ${arr_url.length}`);
		// arr_url.forEach(element => {
		// 	console.log(`element : ${element}`);
		// });
		let boardId = arr_url[arr_url.length-1];
		let security_url = `/api/boards/${boardId}/security`;
		console.log(security_url);
		$.ajax({
			type : "get",
			url : security_url,
			dataType : "json"           
		}).done(function(json, status) {
		  
		  let data = JSON.parse(json);
		  if((data.security == "N" && data.auth == "allow") || (data.security == "Y" && data.auth == "allow") ){
			  location.href = url+"/details?page="+page+"&searchField="+searchField+"&searchText="+searchText;
		  }
		  else{
			  if(data.auth == "not_allow"){
				//alert("비밀글 입니다.");
				$('#alertModal').modal("show");
				let modal = $('#alertModal');
				modal.find('.modal-title').text("Warning - 비밀글 열람");
				modal.find('.modal-title').addClass("text-warning");
				modal.find('.modal-body p').text("비밀글로 설정된  게시글은 작성자 이외에는 열람이 불가합니다.");
			  }
			  
		  }

			
		}).fail(function(e) {
			
		});		

		
	  });



});