package top.zydse.exception;

/**
 * CreateBy: zydse
 * ClassName: CustomizeErrorCode
 * Description:
 *
 * @Date: 2020/3/11
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOND("没有找到你想看的问题，或者问题已经不存在了"),
    QUESTION_ALREADY_DELETED("问题已经被删除了，请重新发布");
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    CustomizeErrorCode(String message){
        this.message = message;
    }
}
