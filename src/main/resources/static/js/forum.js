function comment() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    console.log(questionId + "  " + content);
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (result) {
            console.log(result);
            if (result.code == 200) {
                $("#comment_main").hide();
            } else {
                alert(result.message);
            }
        }
    });
}