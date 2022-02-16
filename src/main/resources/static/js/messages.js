$(document).ready(function(e) {
    // 滚动条
    if($('.lyear-need-scroll')[0]) {
        $('.lyear-need-scroll').each(function() {
            new PerfectScrollbar(this, {
                swipeEasing: false,
                suppressScrollX: true
            });
        });
    }

    // 历史消息(这里只是做拉取历史消息演示，不指定聊天对方的ID)
    $('#history-message').on('click', function() {
        var $listHtml  = $('<div class="history-messages-list" />');
        var $pagesHtml = '';
        var $modalObj   = $('#historyModal');

        jQuery.post('test_chat_message.php', {type: 'history-all', page: 1, version: 4}).done(function(res) {

            var res     = eval('(' + res + ')');
            var message = res.data;

            $modalObj.find('.history-messages-pages').html(res.pages);

            if (200 == res.code) {
                $.each(message, function(index, value){
                    var className = value.type == 'recipient' ? 'history-message-recipient' : 'history-message-sender';

                    $msgHtml = "<div class='history-message-item " + className + "'>" +
                        "  <small>" + value.name + "(" + value.time + ")</small>" +
                        "  <p>" + value.msg + "</p>" +
                        "</div>";

                    $listHtml.append($msgHtml);
                });
            }
        }).fail(function () {
            console.log('服务器错误');
        });

        $modalObj.find('.history-messages-box').html($listHtml).hide().fadeIn('slow');

        $('#historyModal').modal();
    });

    // 清屏
    $('#clear-all').on('click', function() {
        $('.lyear-chat-wrapper').find('.lyear-chat-message-list:visible').html('');
    });

    // 历史消息点击选中
    $('body').on('click', '.history-message-item', function() {
        $(this).addClass('active').siblings().removeClass('active');
    });

    // 历史消息分页操作(这里只是做拉取历史消息演示，不指定聊天对方的ID)
    $('body').on('click', '.history-messages-pages li:not(.disabled) a', function() {
        var $url      = $(this).data('url');
        var $listHtml = $('<div class="history-messages-list" />');
        var $modalObj   = $('#historyModal');

        jQuery.get($url).done(function(res) {
            var res = eval('(' + res + ')');
            var message = res.data,
                tips    = res.msg;

            $modalObj.find('.history-messages-pages').html(res.pages);

            if (200 == res.code) {
                $.each(message, function(index, value){
                    var className = value.type == 'recipient' ? 'history-message-recipient' : 'history-message-sender';

                    $msgHtml = "<div class='history-message-item " + className + "'>" +
                        "  <small>" + value.name + "(" + value.time + ")</small>" +
                        "  <p>" + value.msg + "</p>" +
                        "</div>";

                    $listHtml.append($msgHtml);
                });
            } else {
                console.log(tips);
            }
        }).fail(function () {
            console.log('服务器错误');
        });

        $modalObj.find('.history-messages-box').html($listHtml).hide().fadeIn('slow');
    });

    // 点击聊天
    $('.lyear-chat-users').find('.list-group-item').on('click', function() {
        var $this        = $(this),
            $chatWrapper = $('.lyear-chat-wrapper');

        $this.addClass('active').siblings().removeClass('active');

        var friendId   = $this.data('user-id'),
            friendName = $this.find('.list-chat-user-name').text(),
            friendText = $this.data('autograph'),
            friendImg  = $this.find('.chat-user-avatar').attr('src');

        $('.lyear-chat-user-info').find('.chat-user-avatar').attr('src', friendImg);
        $('#friend-name').text(friendName);
        $('#friend-text').text(friendText);
        $('#lyear-chat-header').attr('data-user-id', friendId);

        var $thisChat = $chatWrapper.find('#chat-by-'+friendId);
        if ($thisChat.length > 0) {
            $thisChat.fadeIn('slow').siblings().hide();
            // 存在，如果有新消息，可追加新消息内容
        } else {
            // 不存在，则拉取最近的五条聊天记录并添加聊天内容框
            var $listHtml = $('<div class="lyear-chat-message-list" id="chat-by-' + friendId + '" />');
            jQuery.post('test_chat_message.php', {type: 'history'}).done(function(res) {
                var res     = eval('(' + res + ')');
                var message = res.data,
                    tips    = res.msg;

                if (200 == res.code) {
                    $.each(message, function(index, value){
                        var className = value.type == 'recipient' ? 'lyear-chat-message-recipient' : 'lyear-chat-message-sender';
                        $msgHtml = "<div class='lyear-chat-message clearfix " + className + "'>" +
                            "  <img class='chat-user-avatar img-avatar' src='" + value.avatar + "' />" +
                            "  <div class='lyear-chat-message-wrapper'>" +
                            "    <div class='lyear-chat-message-content'>" +
                            "      <p>" + value.msg + "</p>" +
                            "    </div>" +
                            "    <div class='lyear-message-details'> <span class='chat-today small'>" + value.time + "</span> </div>" +
                            "  </div>" +
                            "</div>";
                        $listHtml.append($msgHtml);
                    });
                } else {
                    console.log(tips);
                }
            }).fail(function () {
                console.log('服务器错误');
            });
            $chatWrapper.find('.lyear-chat-message-list').hide();
            $chatWrapper.append($listHtml).hide().fadeIn('slow');
        }
        $this.find('.badge').remove();
        $chatWrapper.animate({scrollTop: $chatWrapper[0].scrollHeight}, 500);
    });

    // 绑定表情
    $('.emoj-box').qqFace({
        id: 'facebox', //表情ID
        assign: 'lyear-chat-comment', //赋予到具体位置
    });

    // 表情替换
    var replace_face = function(str) {
        return str.replace( /\[em_([0-9]*)\]/g, "<img src=\"images/face/$1.png\" />");
    };

    // 发送消息(这里只发送到聊天内容中)
    $('.btn-sender').on('click', function(e) {
        clickBtnSend();
    });

    // 回车发送消息
    $('#lyear-chat-comment').keydown(function(event) {
        if (event.keyCode == 13) {
            clickBtnSend();
        }
    });

    // 发送消息通用方法
    var clickBtnSend = function() {
        var friendId = $('#lyear-chat-header').data('user-id'),
            message  = replace_face($('#lyear-chat-comment').val());

        if (message.length <= 0) {
            alert('发送消息不能为空');
            return false;
        }
        sendMessage(friendId, message, 'text');

        // 清空输入框
        $('#lyear-chat-comment').val('');
    };

    // 发送消息通用方法
    var sendMessage = function(friendId, message, type) {
        var $chatBox = $('#chat-by-'+friendId),
            date     = new Date(),
            year     = date.getFullYear(),
            month    = date.getMonth()+1;
        day      = date.getDate();
        hours    = date.getHours(),
            minutes  = date.getMinutes(),
            seconds  = date.getSeconds(),
            datetime = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds,
            $msghtml = '';

        if (type == 'file') {
            // 附件
        } else {
            // 文字
            $msghtml = '<div class="lyear-chat-message clearfix lyear-chat-message-sender">' +
                '  <img class="chat-user-avatar img-circle" src="images/users/avatar.jpg" />' + // 当前用户头像
                '  <div class="lyear-chat-message-wrapper">' +
                '    <div class="lyear-chat-message-content">' +
                '      <p>' + message + '</p>' +
                '    </div>' +
                '    <div class="lyear-message-details"> <span class="chat-today small">' + datetime + '</span> </div>' +
                '  </div>' +
                '</div>';
        }

        // 追加内容
        $chatBox.append($msghtml);
        // 将滚动条置为底部
        $chatBox.parent('.lyear-chat-wrapper').animate({scrollTop: $('.lyear-chat-wrapper')[0].scrollHeight}, 300);
    };

    // 图片发送
    $(document).on('click', '.file-browser', function() {
        var $browser = $(this);
        var file     = $browser.closest('.file-group').find('[type="file"]');

        file.on( 'click', function(e) {
            e.stopPropagation();
        });
        file.trigger('click');
        file.change(function() {
            var files  = this.files,
                friendId = $('#lyear-chat-header').data('user-id');

            $.each(files, function(index, val) {
                if (/image/.test(val.type)) {
                    var reader = new FileReader();
                    reader.readAsDataURL(val);
                    reader.onload = function(e) {
                        var imgHtml = $('<img />');
                        imgHtml.attr("src", this.result);

                        sendMessage(friendId, imgHtml[0].outerHTML, 'images');

                        // 之后操作处理base64上传，省略...
                    }
                } else {
                    console.log('请上传图片');
                }
            });

            // 上传成功，重置file
            file.after(file.clone().val(''));
            file.remove();
        });
    });

    // 搜索(这里只搜索ul中的内容)
    $('#keywords').keydown(function(event) {
        if (event.keyCode == 13) {
            var keyword = $(this).val();
            $('.lyear-chat-users').find('.list-group-item').hide();
            $('.lyear-chat-users').find(".list-chat-user-name").filter(":Contains("+keyword+")").parents('.list-group-item').fadeIn(300);
        }
    });

    // 点击修改我的签名
    $('.my-autograph').on('click', function() {
        var $this = $(this),
            txt   = $this.text(),
            input = $("<input type='text' class='edit-autograph' value='" + txt + "'/>");

        $this.html(input);
        input.click(function() { return false; });
        input.trigger("focus");
        //文本框失去焦点后提交内容，重新变为文本
        input.blur(function() {
            var newtxt = $(this).val();
            if (newtxt != txt) {
                // 假装ajax调用后台修改成功
                $this.html(newtxt);
            } else {
                $this.html(newtxt);
            }
        });
    });

    // 点击展开用户列表
    $('.lyear-chat-toggler').on('click', function() {
        $('.lyear-chat-side').toggleClass('lyear-chat-user-open');
    })
});