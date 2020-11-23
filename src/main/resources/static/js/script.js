/**
 * 
 */ 
$(".reply_write input[type=submit]").click(function(e){
	e.preventDefault();
	console.log("clicked!!");
	
	let queryString = $(".reply_write").serialize();
	console.log(`queryString : ${queryString}`);
	
	let url = $(".reply_write").attr("action");
	console.log(`url : ${url}`);
	
	$.ajax({
	    type : "post" ,
	    url : url,
	    data : queryString,
	    dataType : "json"	      
	  }	).done(function(data, status) {
	      console.log(data);
	      $(".reply-tot-cnt").html(`<strong>${data.board.count_of_reply}개의 의견</strong>`); 
	      let replyTemplate = $("#replyTemplate").html();
	      let template = replyTemplate.format(data.user.username, data.createdate, data.content, data.board.id ,data.id);
	      $(".board-comment-articles").append(template);
	      $(".reply_write")[0].reset();
	  }).fail(function(e){
	    
	  });	
});

$("a.link-delete-article").click(function(e){
    e.preventDefault();
    var deleteArticle = $(this);
    let url = deleteArticle.attr("href");
    console.log(`url : ${url}`);
    
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
        console.log(data);
        if(data.valid){
            $(".reply-tot-cnt").html(`<strong>${data.count}개의 의견</strong>`);
            deleteArticle.closest(".card-body").remove();
        }else{
            alert(data.errorMessage);
        }
    });
    
});

String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};