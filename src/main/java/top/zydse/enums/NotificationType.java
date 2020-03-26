package top.zydse.enums;

/**
 * CreateBy: zydse
 * ClassName: NotificationType
 * Description:
 *
 * @Date: 2020/3/16
 */
public enum NotificationType {
    ANSWER_QUESTION(1, "回复了问题"),
    ANSWER_COMMENT(2, "回复了评论"),
    THUMB_UP(3, "点赞了评论");

    private int type;
    private String name;

    NotificationType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String nameOf(Integer type) {
        for (NotificationType value : NotificationType.values()) {
            if(value.getType() == type){
                return value.getName();
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
