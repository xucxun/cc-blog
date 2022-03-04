// 激活
function setActivate(btn,userId) {
    $.post(
        "/admin/userManage/activate",
        {"id": userId},
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

// 禁用
function setDisabled(btn,userId) {
    if($(btn).hasClass("btn-danger")) {
        //禁用
        $.post(
            "/admin/userManage/disabled",
            {"id": userId},
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
        //取消禁用
        $.post(
            "/admin/userManage/unDisabled",
            {"id": userId},
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

//设为普通用户
function setUser(btn,userId){
    //设为普通用户
    $.post(
         "/admin/userManage/setUser",
        {"id": userId},
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

// 设为管理员
function setAdmin(btn,userId) {
        //设为管理员
        $.post(
            "/admin/userManage/setAdmin",
            {"id": userId},
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

// 设为超级管理员
function setSuperAdmin(btn,userId) {
    //设为超级管理员
    $.post(
        "/admin/userManage/setSuperAdmin",
        {"id": userId},
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


