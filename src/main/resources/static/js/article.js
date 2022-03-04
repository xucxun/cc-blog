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
                showNotify(data.msg, 'danger', 1000);
            }
        }
    );

}
$(function(){
    // $("#topBtn").click(setTop);
    // $("#marrowBtn").click(setMarrow);
    $("#deleteBtn").click(setDelete);
    $("#delBtn").click(setDelete);
});

// 置顶
function setTop(btn,articleId) {
    if($(btn).hasClass("btn-primary")) {
        //置顶
        $.post(
            "/article/top",
            {"id": articleId},
            function (data) {
                //从服务器返回的json字符串中取出code值
                data = $.parseJSON(data);
                if (data.code == 0) {
                    window.location.reload();
                } else {
                    showNotify(data.msg, 'danger', 1000);
                }
            }
        );
    }else{
        //取消置顶
        $.post(
             "/article/untop",
            {"id": articleId},
            function (data) {
                //从服务器返回的json字符串中取出code值
                data = $.parseJSON(data);
                if (data.code == 0) {
                    window.location.reload();
                } else {
                    showNotify(data.msg, 'danger', 1000);
                }
            }
        );
    }
}


// 加精
function setMarrow(btn,articleId) {
    if($(btn).hasClass("btn-primary")) {
        //加精
        $.post(
            "/article/marrow",
            {"id": articleId},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    window.location.reload();
                } else {
                    showNotify(data.msg, 'danger', 1000);
                }
            }
        );
    }else{
        //取消加精
        $.post(
            "/article/unmarrow",
            {"id": articleId},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    window.location.reload();
                } else {
                    showNotify(data.msg, 'danger', 1000);
                }
            }
        );
    }
}

function setDelete() {
       var flag = $('#flag').val();
        $.post(
            "/article/delete",
            {"id": $("#articleId").val()},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0 && flag == 0) {
                    location.href = "/index";
                }else if (data.code == 0 && flag == 1){
                    showNotify(data.msg, 'success', 1000);
               }else {
                    showNotify(data.msg, 'danger', 1000);
                }

                setTimeout(function(){
                    if(data.code == 0 && flag == 1) {
                        window.location.reload();
                    }
                }, 1000);
            }
        );
}


/*
 * 提取通用的通知消息方法
 * @param $msg 提示信息
 * @param $type 提示类型:'info', 'success', 'warning', 'danger'
 * @param $delay 毫秒数，例如：1000
 * @param $icon 图标，例如：'fa fa-user' 或 'glyphicon glyphicon-warning-sign'
 * @param $from 'top' 或 'bottom' 消息出现的位置
 * @param $align 'left', 'right', 'center' 消息出现的位置
 */
function showNotify($msg, $type, $delay, $icon, $from, $align) {
    $type  = $type || 'info';
    $delay = $delay || 1000;
    $from  = $from || 'top';
    $align = $align || 'right';
    $enter = $type == 'danger' ? 'animated shake' : 'animated fadeInUp';

    $.notify({
            icon: $icon,
            message: $msg
        },
        {
            element: 'body',
            type: $type,
            allow_dismiss: false,
            newest_on_top: true,
            showProgressbar: false,
            placement: {
                from: $from,
                align: $align
            },
            offset: 20,
            spacing: 10,
            z_index: 10800,
            delay: $delay,
            animate: {
                enter: $enter,
                exit: 'animated fadeOutDown'
            }
        });
}