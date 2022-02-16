$(function(){
	$("#sendBtn").click(send);
	$(".close").click(delete_msg);
});

function send() {
	$("#sendModal").modal("hide");

	var receiverName = $("#recipient-name").val();
	var content = $("#message-text").val().replace(/\r\n/g, '<br/>').replace(/\n/g, '<br/>').replace(/\s/g, ' ');
	$.post(
		"/messages/send",
		{"receiverName":receiverName,"content":content},
		function(data) {
			data = $.parseJSON(data);
			if(data.code == 0) {
				$("#hintBody").text("发送成功!");
			} else {
				$("#hintBody").text(data.msg);
			}
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				location.reload();
			}, 2000);
		}
	);
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}