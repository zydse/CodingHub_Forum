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

    if ($("#comment-" + id).hasClass("in")) {
        $("#comment-" + id).removeClass("in");
        e.setAttribute("style", "color: #999");
    } else {
        $.getJSON("/comment/" + id, function (response) {
            console.log(response.data);
        });
        $("#comment-" + id).addClass("in");
        e.setAttribute("style", "color: #499ef3");
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
            console.log(result);
            if (result.code == 200 && commentType == 1) {
                $("#comment_main").hide();
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