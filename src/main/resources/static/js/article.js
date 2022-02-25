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
$(function(){
    $("#topBtn").click(setTop);
    $("#marrowBtn").click(setMarrow);
    $("#deleteBtn").click(setDelete);
    $("#delBtn").click(setDelete);
});

// 置顶
function setTop() {
    if($("#topBtn").hasClass("btn-danger")) {
        //置顶
        $.post(
            "/article/top",
            {"id": $("#articleId").val()},
            function (data) {
                //从服务器返回的json字符串中取出code值
                data = $.parseJSON(data);
                if (data.code == 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
    }else{
        //取消置顶
        $.post(
             "/article/untop",
            {"id": $("#articleId").val()},
            function (data) {
                //从服务器返回的json字符串中取出code值
                data = $.parseJSON(data);
                if (data.code == 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
    }
}


// 加精
function setMarrow() {
    if($("#marrowBtn").hasClass("btn-danger")) {
        //加精
        $.post(
            "/article/marrow",
            {"id": $("#articleId").val()},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
    }else{
        //取消加精
        $.post(
            "/article/unmarrow",
            {"id": $("#articleId").val()},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
    }
}

// 删除博客
// function setDelete() {
//     var flag = confirm("您确认要删除博客吗？");
//
//     if(flag) {
//         $.post(
//             "/article/delete",
//             {"id": $("#articleId").val()},
//             function (data) {
//                 data = $.parseJSON(data);
//                 if (data.code == 0) {
//                     location.href = "/index";
//                 } else {
//                     alert(data.msg);
//                 }
//             }
//         );
//     }
// }
function setDelete() {
        $.post(
            "/article/delete",
            {"id": $("#articleId").val()},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    location.href = "/index";
                } else {
                    alert(data.msg);
                }
            }
        );
}