package top.zydse.model;

public class Tag {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TAG.ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TAG.TAG_TYPE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    private String tagType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TAG.TAG_NAME
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    private String tagName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TAG.COUNT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    private Long count;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TAG.ID
     *
     * @return the value of TAG.ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TAG.ID
     *
     * @param id the value for TAG.ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TAG.TAG_TYPE
     *
     * @return the value of TAG.TAG_TYPE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public String getTagType() {
        return tagType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TAG.TAG_TYPE
     *
     * @param tagType the value for TAG.TAG_TYPE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public void setTagType(String tagType) {
        this.tagType = tagType == null ? null : tagType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TAG.TAG_NAME
     *
     * @return the value of TAG.TAG_NAME
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TAG.TAG_NAME
     *
     * @param tagName the value for TAG.TAG_NAME
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TAG.COUNT
     *
     * @return the value of TAG.COUNT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public Long getCount() {
        return count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TAG.COUNT
     *
     * @param count the value for TAG.COUNT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public void setCount(Long count) {
        this.count = count;
    }
}