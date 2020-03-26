package top.zydse.model;

public class Comment {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.ID
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.PARENT_ID
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    private Long parentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.TYPE
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.REVIEWER
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    private Long reviewer;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.GMT_CREATE
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    private Long gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.GMT_MODIFIED
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    private Long gmtModified;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.THUMB_COUNT
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    private Integer thumbCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.CONTENT
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    private String content;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column COMMENT.SUB_COMMENT_COUNT
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    private Integer subCommentCount;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column COMMENT.ID
     *
     * @return the value of COMMENT.ID
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column COMMENT.ID
     *
     * @param id the value for COMMENT.ID
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column COMMENT.PARENT_ID
     *
     * @return the value of COMMENT.PARENT_ID
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column COMMENT.PARENT_ID
     *
     * @param parentId the value for COMMENT.PARENT_ID
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column COMMENT.TYPE
     *
     * @return the value of COMMENT.TYPE
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column COMMENT.TYPE
     *
     * @param type the value for COMMENT.TYPE
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column COMMENT.REVIEWER
     *
     * @return the value of COMMENT.REVIEWER
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public Long getReviewer() {
        return reviewer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column COMMENT.REVIEWER
     *
     * @param reviewer the value for COMMENT.REVIEWER
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public void setReviewer(Long reviewer) {
        this.reviewer = reviewer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column COMMENT.GMT_CREATE
     *
     * @return the value of COMMENT.GMT_CREATE
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column COMMENT.GMT_CREATE
     *
     * @param gmtCreate the value for COMMENT.GMT_CREATE
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column COMMENT.GMT_MODIFIED
     *
     * @return the value of COMMENT.GMT_MODIFIED
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public Long getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column COMMENT.GMT_MODIFIED
     *
     * @param gmtModified the value for COMMENT.GMT_MODIFIED
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column COMMENT.THUMB_COUNT
     *
     * @return the value of COMMENT.THUMB_COUNT
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public Integer getThumbCount() {
        return thumbCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column COMMENT.THUMB_COUNT
     *
     * @param thumbCount the value for COMMENT.THUMB_COUNT
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public void setThumbCount(Integer thumbCount) {
        this.thumbCount = thumbCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column COMMENT.CONTENT
     *
     * @return the value of COMMENT.CONTENT
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column COMMENT.CONTENT
     *
     * @param content the value for COMMENT.CONTENT
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column COMMENT.SUB_COMMENT_COUNT
     *
     * @return the value of COMMENT.SUB_COMMENT_COUNT
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public Integer getSubCommentCount() {
        return subCommentCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column COMMENT.SUB_COMMENT_COUNT
     *
     * @param subCommentCount the value for COMMENT.SUB_COMMENT_COUNT
     *
     * @mbg.generated Tue Mar 17 22:58:32 CST 2020
     */
    public void setSubCommentCount(Integer subCommentCount) {
        this.subCommentCount = subCommentCount;
    }
}