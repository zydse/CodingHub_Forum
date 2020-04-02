package top.zydse.model;

public class Permission {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PERMISSION.ID
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PERMISSION.TYPE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    private String type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PERMISSION.URL
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    private String url;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PERMISSION.PER_CODE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    private String perCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PERMISSION.AVAILABLE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    private Integer available;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PERMISSION.GMT_CREATE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    private Long gmtCreate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PERMISSION.ID
     *
     * @return the value of PERMISSION.ID
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PERMISSION.ID
     *
     * @param id the value for PERMISSION.ID
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PERMISSION.TYPE
     *
     * @return the value of PERMISSION.TYPE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PERMISSION.TYPE
     *
     * @param type the value for PERMISSION.TYPE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PERMISSION.URL
     *
     * @return the value of PERMISSION.URL
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PERMISSION.URL
     *
     * @param url the value for PERMISSION.URL
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PERMISSION.PER_CODE
     *
     * @return the value of PERMISSION.PER_CODE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public String getPerCode() {
        return perCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PERMISSION.PER_CODE
     *
     * @param perCode the value for PERMISSION.PER_CODE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public void setPerCode(String perCode) {
        this.perCode = perCode == null ? null : perCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PERMISSION.AVAILABLE
     *
     * @return the value of PERMISSION.AVAILABLE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public Integer getAvailable() {
        return available;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PERMISSION.AVAILABLE
     *
     * @param available the value for PERMISSION.AVAILABLE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public void setAvailable(Integer available) {
        this.available = available;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PERMISSION.GMT_CREATE
     *
     * @return the value of PERMISSION.GMT_CREATE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PERMISSION.GMT_CREATE
     *
     * @param gmtCreate the value for PERMISSION.GMT_CREATE
     *
     * @mbg.generated Wed Apr 01 15:39:55 CST 2020
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}