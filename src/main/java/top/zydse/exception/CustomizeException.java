package top.zydse.exception;

/**
 * CreateBy: zydse
 * ClassName: CustomizeException
 * Description:
 *
 * @Date: 2020/3/11
 */
public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
