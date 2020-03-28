package top.zydse.enums;

/**
 * CreateBy: zydse
 * ClassName: CommentTypeEnum
 * Description:
 *
 * @Date: 2020/3/11
 */
public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if(value.getType().equals(type))
                return true;
        }
        return false;
    }
}
