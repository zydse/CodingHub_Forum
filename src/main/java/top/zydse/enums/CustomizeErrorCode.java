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
    AUTHORITY_ERROR(2006, "抱歉，你当前登录的用户没有权限查看这些内容"),
    ALI_SERVER_ERROR(2007, "阿里云短信服务好像有点问题，程序员正在抢修"),
    BAD_REQUEST(4004, "你的请求看起来有点问题"),
    COMMENT_TARGET_NOT_FOUND(3001, "未选中问题或评论进行回复"),
    COMMENT_TYPE_NOT_FOUND(3002, "请告诉我，你要回复问题还是评论"),
    COMMENT_NOT_FOUND(3003, "对不起，你要回复的评论不存在了"),
    CONTENT_IS_EMPTY_OR_TO_SHORT(3004, "你的评论内容可能迷路了，或者你回复的内容太短了"),
    QUESTION_NOT_FOUND(3005, "没有找到你想看的问题，或者问题已经不存在了"),
    QUESTION_ALREADY_DELETED(3006, "问题已经被删除了，请重新发布"),
    NOTIFICATION_NOT_FOUND(3007, "通知已经不存在，或者迷路了"),
    DUPLICATE_USERNAME(4001, "用户名已经被注册过了，请你换个名字试试");

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
