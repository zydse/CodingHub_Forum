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
            if (result.code === 200 && commentType === 1) {
                window.location.reload();
            } else if (result.code === 200 && commentType === 2) {
                $("input[name=input-" + parentId + "]").val("");
                var $collapse = $("#collapse-" + parentId);
                var $subCount = $("#subCount-" + parentId);
                var count = parseInt($subCount.text());
                $subCount.text(count + 1);
                $collapse.click();
            } else {
                alert(result.message);
            }
        }
    });
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
        $.getJSON("/comment/list/" + id, function (response) {
            var inputElem = commentContainer.get(0).lastElementChild;
            commentContainer.empty();
            var $subCount = $("#subCount-" + id);
            $subCount.text(response.data.length);
            $.each(response.data, function (index, subComment) {
                var $mediaLeft = $("<div/>", {
                    class: "media-left"
                });
                var $a = $("<a/>", {
                    href: "#"
                });
                var $avatar = $("<img/>", {
                    class: "media-object",
                    src: subComment.user.avatarUrl
                });
                var $mediaBody = $("<div/>", {
                    class: "media-body"
                });
                var $name = $("<h5/>", {
                    class: "media-heading text_desc",
                    html: subComment.user.name
                });
                var $text = $("<div/>", {
                    html: subComment.content
                });
                var $operate = $("<div/>", {
                    class: "comment_operate"
                });
                var $date = $("<span/>", {
                    class: "text_desc pull-right",
                    html: getMyDate(subComment.gmtModified)
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

function thumbComment(e) {
    var id = $(e).data("id");
    var active = $(e).data("active");
    if ($(e).hasClass("active") || active !== 1) {
        return;
    }
    $.ajax({
        type: 'POST',
        url: '/comment/thumb',
        dataType: 'json',
        data: {
            commentId: id
        },
        success: function (result) {
            if (result.code !== 200) {
                alert(result.message);
            } else {
                $(e).addClass("active");
                var $thumbCount = $("#thumbCount-" + id);
                $thumbCount.addClass("active");
                var count = parseInt($thumbCount.text());
                $thumbCount.text(count + 1);
            }
        }
    });
}

function getMyDate(str) {
    var oDate = new Date(str), oHour = oDate.getHours(), oMin = oDate.getMinutes(), oSen = oDate.getSeconds(),
        oYear = oDate.getFullYear(),
        oMonth = oDate.getMonth() + 1,
        oDay = oDate.getDate();//最后拼接时间
    return oYear + '-' + addZero(oMonth) + '-' + addZero(oDay) + ' ' + addZero(oHour) + ':' + addZero(oMin) + ':' + addZero(oSen);
}

function addZero(num) {
    if (parseInt(num) < 10) {
        num = '0' + num;
    }
    return num;
}