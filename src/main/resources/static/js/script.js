/**
 * 
 */ 
$(".reply_write input[type=submit]").click(function(e){
	e.preventDefault();
  let content = $(".reply_write").find("textarea[name='content']").val();
  console.log(`content : ${content}`);
  $(".invalid").addClass("d-none");
  if(content == '<p><br></p>'){
    content = "";
  }
  if( !content ){ 
        //$(".note-status-output").removeClass("d-none");
        $(".note-status-output").html(
            '<div class="alert alert-danger">' +
            '내용을 입력해주세요.' +
            '</div>'          
        );
      return;
  }
  let queryString = $(".reply_write").serialize();
	let url = $(".reply_write").attr("action");
	console.log(`post url : ${url}`);
	$.ajax({
	    type : "post" ,
	    url : url,
	    data : queryString,
	    dataType : "json"	      
	  }	).done(function(data, status) {
      console.log(data);
	      $(".reply-tot-cnt").html(`<strong>${data.count_of_reply}개의 의견</strong>`); 
	      let replyTemplate = $("#replyTemplate").html();
	     // let createdate = moment(data.createdate).format('YYYY-MM-DD HH:mm');
	     // let content = data.content.replace('\r\n','<br>');
	      let template = replyTemplate.format(data.username, data.createdate, data.content, data.boardid ,data.id);
        $(".board-comment-articles").append(template);
        $(".note-status-output").html('');
        $(".reply_write")[0].reset();
        $('.summernote').summernote('reset');
	  }).fail(function(e){
	    
	  });	
});

// 댓글 업데이트 폼
$(".link-modify-article").on("click", function(e){
  e.preventDefault();
  let _this = $(this);  
  onReplyUpdate(_this);
  
});

$(".link-delete-article").on("click", function(e) {
  e.preventDefault();
  let _this = $(this);
  onReplyDelete(_this);
})

$("#content").on("change keyup paste", function(e){
    let length = $(this).val().length; 
    if(length > 2){
        console.log("2자 이상");
        $("#reply-submit").removeClass("disabled");
    }else{
       console.log("2자 이하");    
       $("#reply-submit").addClass("disabled");
    }  
});

function onReplyUpdate(_this){
    let updateArticle = _this;
    let url  = updateArticle.attr("href");
    console.log("get url :"+url);
    $.ajax({
        type : "get",
        url : url,
        dataType : "json"           
    }).done(function(data, status) {
      console.log(data);
//      console.log(`content : ${data.content}`);
          if(updateArticle.html() == "수정"){
            let replyTemplate = $("#replyUpdateTemplate").html();
            //let content = data.content.replace('\r\n','<br>');
            let template = replyTemplate.format(data.boardid,data.id, data.content);
            updateArticle.closest("div").append(template);
            updateArticle.closest("div").addClass("alert-light");
            updateArticle.html("닫기");
            $('.summernote').summernote({
                  toolbar: [
                    ['style', ['bold', 'italic', 'underline', 'clear']],
                    ['font', ['strikethrough', 'superscript', 'subscript']],
                    ['fontsize', ['fontsize']],
                    ['color', ['color']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['height', ['height']]
                  ],              
                  lang: 'ko-KR',
                  tabsize: 2,
                  height: 100
            });            
        }else{
            updateArticle.closest("div").removeClass("alert-light");
            updateArticle.html("수정");
            updateArticle.closest("div").find(".article1").remove();
        }      
        
    }).fail(function(e) {
        
    });
    
}

function onReplyDelete(_this){ 
  //var deleteArticle = $(this);
  var deleteArticle = _this;
  let url = deleteArticle.attr("href");
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
//      console.log(data);
      if(data.valid){
          $(".reply-tot-cnt").html(`<strong>${data.count}개의 의견</strong>`);
          deleteArticle.closest(".card-body").remove();
      }else{
          alert(data.errorMessage);
      }
  });  
}

function deleteReply(e){
  e.preventDefault();
//  console.log(e);
//  console.log(`e.path[3] : ${e.path[3]}`)
//  console.log(e.srcElement.className);
//    console.log(`boardid : ${boardId}, replyid : ${replyId}`);
//    console.log($('link-delete-article'));
//    $('link-delete-article').trigger("click");
  let _this = $(e.srcElement);
  console.log(_this.attr("href"));
  onReplyDelete(_this);
}

function updateReply(e){
  e.preventDefault();
//  console.log(e);
//  console.log(`e.path[3] : ${e.path[3]}`)
//  console.log(e.srcElement.className);
//    console.log(`boardid : ${boardId}, replyid : ${replyId}`);
//    console.log($('link-delete-article'));
//    $('link-delete-article').trigger("click");
  let _this = $(e.srcElement);
  onReplyUpdate(_this);
}

function updateReplySubmit(e){
    let queryString = $(".reply_update").serialize();
    let url = $(".reply_update").attr("action");    
    let _this = $(e.srcElement);
    let replyForm = e;
//    console.log(queryString);
//    console.log(url);
    $.ajax({
      type : "put" ,
      url : url,
      data : queryString,
      dataType : "json"         
    } ).done(function(data, status) {
        console.log(data);
        let content = data.content.replaceAll('\r\n','<br>');

        _this.closest(".card-body").find(".article").find(".card-subtitle").html(data.modifydate);
        _this.closest(".card-body").find(".article").find(".card-text").html(content);
        _this.closest(".card-body").find(".article").find(".link-modify-article").html("수정");          
        _this.closest(".card-body").removeClass("alert-light");        
        _this.closest(".article1").remove();
//        updateReply(obj);
    }).fail(function(e){
      
    });    
}



String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};