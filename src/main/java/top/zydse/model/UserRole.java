package top.zydse.model;

public class UserRole {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER_ROLE.ID
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER_ROLE.USER_ID
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER_ROLE.ROLE_ID
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    private Integer roleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column USER_ROLE.GMT_CREATE
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    private Long gmtCreate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER_ROLE.ID
     *
     * @return the value of USER_ROLE.ID
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER_ROLE.ID
     *
     * @param id the value for USER_ROLE.ID
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER_ROLE.USER_ID
     *
     * @return the value of USER_ROLE.USER_ID
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER_ROLE.USER_ID
     *
     * @param userId the value for USER_ROLE.USER_ID
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER_ROLE.ROLE_ID
     *
     * @return the value of USER_ROLE.ROLE_ID
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER_ROLE.ROLE_ID
     *
     * @param roleId the value for USER_ROLE.ROLE_ID
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column USER_ROLE.GMT_CREATE
     *
     * @return the value of USER_ROLE.GMT_CREATE
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column USER_ROLE.GMT_CREATE
     *
     * @param gmtCreate the value for USER_ROLE.GMT_CREATE
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}