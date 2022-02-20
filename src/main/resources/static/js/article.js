//点赞
function like(btn, entityType, entityId, entityUserId,articleId){
    $.post(
        "/like",
        {"entityType":entityType,"entityId":entityId,"entityUserId":entityUserId,"articleId":articleId},
        function(data) {
            data = $.parseJSON(data);
            if(data.code == 0) {
                $(btn).children("i").eq(1).text(data.likeCount);
                // $(btn).children("b").text(data.likeStatus==1?'已赞':"赞");
                if(data.likeStatus==1){
                    $(btn).children("i").css("color","#33cabb");
                }else {
                    $(btn).children("i").css("color","#868e96");
                }
            } else {
                alert(data.msg);
            }
        }
    );

}