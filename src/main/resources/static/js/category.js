$(function(){
    $("#addBtn").click(addCategory);
    $("#deleteBtn").click(setDelete);
    $('#editBtn').click(edit);
});

//新增分类
function addCategory() {
    $("#addModal").modal("hide");

    // 获取标题和内容
    var name = $("#recipient-name").val();
    var description = $("#message-text").val();
    // 发送异步请求(POST)
    $.post(
        "/admin/categoryManage/save",
        {"name":name,"description":description},
        function(data) {
            data = $.parseJSON(data);
            if(data.code == 0) {
                showNotify(data.msg, 'success', 1000);
            }else{
                showNotify(data.msg, 'danger', 1000);
            }
            setTimeout(function(){
                if(data.code == 0) {
                    window.location.reload();
                }
            }, 1000);
        }
    );
}

// 前台显示
function setDisplay(btn,id) {
    if($(btn).hasClass("btn-primary")) {
        //前台显示
        $.post(
            "/admin/categoryManage/display",
            {"id": id},
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
        $.post(
            "/admin/categoryManage/unDisplay",
            {"id": id},
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

function toEdit(btn,id) {
    $.get(
       "/admin/categoryManage/" + id,
       function (result) {
           if (result.success) {
               //向模态框中传值
                $('#edit_id').val(id);
                $('#edit-name').val(result.data.name);
                $('#description-text').val(result.data.description);
            } else {
               showNotify(result.data.msg, 'danger', 1000);
            }
        }
    );
}



//提交修改
function edit() {
    $("#editModal").modal("hide");
    //获取模态框数据
    var id = $('#edit_id').val();
    var name = $('#edit-name').val();
    var description = $('#description-text').val();
    $.post(
       "/admin/categoryManage/edit",
        {"id": id,"name":name,"description":description},
       function (data) {
           data = $.parseJSON(data);
            if (data.code == 200) {
                showNotify(data.msg, 'success', 1000);
            } else {
                showNotify(data.msg, 'danger', 1000);
            }
           setTimeout(function(){
               if(data.code == 200) {
                   reset();
                   window.location.reload();
               }
           }, 1000);
        }
    );
}

function reset() {
    $('#edit_id').val("");
    $('#edit-name').val("");
    $('#description-text').val("");
}

//删除分类
function setDelete() {
    $.post(
        "/admin/categoryManage/delete",
        {"id": $("#categoryId").val()},
        function (data) {
            data = $.parseJSON(data);
            if(data.code == 0) {
                showNotify(data.msg, 'success', 1000);
            }else{
                showNotify(data.msg, 'danger', 1000);
            }
            setTimeout(function(){
                if(data.code == 0) {
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