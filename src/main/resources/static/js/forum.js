/**
 * 发起异步的一级评论请求
 */
function comment() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    if (!content) {
        alert("请先输入评论内容");
        return;
    }
    sendComment(questionId, content, 1);
}

function subComment(e) {
    var id = $(e).data("id");
    var content = $("input[name=input-" + id + "]").val();
    if (!content) {
        alert("请先输入评论内容");
        return;
    }
    sendComment(id, content, 2);
}

/**
 * 点击评论按钮展开二级评论列表
 * @param e
 */
function collapseComment(e) {
    var id = $(e).data("id");
    var commentContainer = $("#comment-" + id);

    if (commentContainer.hasClass("in")) {
        commentContainer.removeClass("in");
        var inputElem = commentContainer.get(0).lastElementChild;
        commentContainer.empty();
        commentContainer.append($(inputElem));
        $(e).removeClass("active");
    } else {
        $.getJSON("/comment/" + id, function (response) {
            var inputElem = commentContainer.get(0).lastElementChild;
            commentContainer.empty();
            $.each(response.data, function (index, comment) {
                var $mediaLeft = $("<div/>", {
                    class: "media-left"
                });
                var $a = $("<a/>", {
                    href: "#"
                });
                var $avatar = $("<img/>", {
                    class: "media-object",
                    src: comment.user.avatarUrl
                });
                var $mediaBody = $("<div/>", {
                    class: "media-body"
                });
                var $name = $("<h5/>", {
                    class: "media-heading text_desc",
                    html: comment.user.name
                });
                var $text = $("<div/>", {
                    html: comment.content
                });
                var $operate = $("<div/>", {
                    class: "comment_operate"
                });
                var $date = $("<span/>", {
                    class: "text_desc pull-right",
                    html: getMyDate(comment.gmtModified)
                });
                $mediaBody.append($name).append($text).append($operate.append($date));
                $a.append($avatar);
                $mediaLeft.append($a);
                var elem = $("<div/>", {
                    class: "col-lg-12 col-md-12 col-sm-12 col-xs-12 media comment_section"
                });
                elem.append($mediaLeft).append($mediaBody);
                commentContainer.append(elem);
            });
            commentContainer.append($(inputElem));
            $("#comment-" + id).addClass("in");
            $(e).addClass("active");
        });
    }
}

/**
 * 发送评论内容到服务端
 * @param parentId
 * @param content
 * @param commentType
 */
function sendComment(parentId, content, commentType) {
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "parentId": parentId,
            "content": content,
            "type": commentType
        }),
        success: function (result) {
            if (result.code == 200 && commentType == 1) {
                $("#comment_main").hide();
            } else if (result.code == 200 && commentType == 2) {
                $("input[name=input-" + parentId + "]").val("");
                $("#collapse-" + parentId).click();
            } else if (result.code == 2002) {
                var isAccept = confirm(result.message);
                if (isAccept) {
                    window.open("https://github.com/login/oauth/authorize?client_id=6f77efebf08b5cde324b&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                    window.localStorage.setItem("closable", "true");
                }
            } else {
                alert(result.message);
            }
        }
    });
}

function getMyDate(str) {
    /* + ' ' + addZero(oHour) + ':' + addZero(oMin) + ':' + addZero(oSen);*/
    /*oHour = oDate.getHours(),oMin = oDate.getMinutes(),oSen = oDate.getSeconds(),*/
    var oDate = new Date(str), oHour = oDate.getHours(), oMin = oDate.getMinutes(), oSen = oDate.getSeconds(),
        oYear = oDate.getFullYear(),
        oMonth = oDate.getMonth() + 1,
        oDay = oDate.getDate();//最后拼接时间
    return oYear + '-' + addZero(oMonth) + '-' + addZero(oDay)+ ' ' + addZero(oHour) + ':' + addZero(oMin) + ':' + addZero(oSen);
}

//补0操作
function addZero(num) {
    if (parseInt(num) < 10) {
        num = '0' + num;
    }
    return num;
}