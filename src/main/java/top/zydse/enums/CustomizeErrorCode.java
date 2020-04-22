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
    ALI_SERVER_ERROR(2007, "请检查你输入的手机号，如果没有问题，那阿里云短信服务好像有点问题，程序员正在抢修"),
    DUPLICATE_PHONE_NUMBER(2008, "这个手机号已经注册过账户了，请换一个手机号注册吧！"),
    BAD_REQUEST(2008, "你的请求看起来有点问题"),
    ACCOUNT_ERROR(2009, "你的账户看起来有点问题"),
    COMMENT_TARGET_NOT_FOUND(3001, "未选中问题或评论进行回复"),
    COMMENT_TYPE_NOT_FOUND(3002, "请告诉我，你要回复问题还是评论"),
    COMMENT_NOT_FOUND(3003, "对不起，你要回复的评论不存在了"),
    CONTENT_IS_EMPTY_OR_TO_SHORT(3004, "你的评论内容可能迷路了，或者你回复的内容太短了"),
    QUESTION_NOT_FOUND(3005, "没有找到你想看的问题，或者问题已经不存在了"),
    QUESTION_ALREADY_DELETED(3006, "问题已经被删除了，请重新发布"),
    NOTIFICATION_NOT_FOUND(3007, "通知已经不存在，或者迷路了"),
    TAG_NOT_FOUND(3008, "你发布的问题中有错误标签"),
    SENSITIVE_WORD_FOUND_IN_TITLE(3009, "你的提问标题中含有敏感词汇，为了共同建立更良好的社区环境，请修改你的标题"),
    SENSITIVE_WORD_FOUND_IN_DESCRIPTION(3010, "你提交的内容中包含了敏感词汇，为了共同建立更良好的社区环境，请修改你的提问描述"),
    DUPLICATE_THUMB_UP(3011, "这条回复你已经点赞过了"),
    THUMB_UP_SELF(3012, "你不可以点赞自己的回复"),
    QUESTION_INFO_ERROR(3013, "你输入的问题信息可能有问题，请仔细检查是否符合要求"),
    DUPLICATE_USERNAME(4001, "用户名已经被注册过了，请你换个名字试试"),
    VERIFICATION_CODE_ERROR(4002, "验证码有点问题，请重试"),
    VERIFICATION_CODE_INACTIVE(4003, "你输入短信验证码错误，或者你太长时间没有操作，验证码已经失效了"),
    USERNAME_INCORRECT(4004, "你看起来好像还没有注册过，请先注册后再登录"),
    PASSWORD_INCORRECT(4005, "你输入的密码错了，请重新输入后登录"),
    CAPTCHA_CODE_ERROR(4006, "你的登录验证码似乎错了呢"),
    COOKIE_ERROR(4006, "登录中的Cookie遇到了一点问题，现在你可以返回主页，或者重新登录了"),
    PASSWORD_RETYPE_INCORRECT(4007, "你两次输入的验证码不同，请检查后重新修改"),
    ORIGINAL_PASSWORD_INCORRECT(4008, "看起来你输入的原密码错了，请仔细想想再修改吧！"),
    SENSITIVE_WORD_FOUND_IN_USERNAME(4009, "你的用户名中包含了敏感词汇，请换一个吧！");

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
