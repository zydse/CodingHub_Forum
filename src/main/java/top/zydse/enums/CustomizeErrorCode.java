package top.zydse.enums;

/**
 * CreateBy: zydse
 * ClassName: CustomizeErrorCode
 * Description:
 *
 * @Date: 2020/3/11
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {
    NO_LOGIN(2002, "当前操作需要登录才可使用"),
    SYSTEM_ERROR(2005, "程序员太菜了，这里写了一个BUG"),
    COMMENT_TARGET_NOT_FOUND(3001, "未选中问题或评论进行回复"),
    BAD_REQUEST(4004, "你的请求看起来有点问题"),
    QUESTION_NOT_FOUND(9001, "没有找到你想看的问题，或者问题已经不存在了"),
    QUESTION_ALREADY_DELETED(9002, "问题已经被删除了，请重新发布"),
    COMMENT_TYPE_NOT_FOUND(3002, "请告诉我，你要回复问题还是评论"),
    COMMENT_NOT_FOUND(3003, "对不起，你要回复的评论不存在了");

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
