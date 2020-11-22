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
	  	  //console.log(`status : ${status}`);
	      let replyTemplate = $("#replyTemplate").html();
	      let template = replyTemplate.format(data.user.username, data.createdate, data.content,data.id);
	      $(".board-comment-articles").append(template);
	      $(".reply_write")[0].reset();
	  }).fail(function(e) {
	  	
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