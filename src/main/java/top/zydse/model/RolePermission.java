package top.zydse.model;

public class RolePermission {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ROLE_PERMISSION.ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ROLE_PERMISSION.ROLE_ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    private Integer roleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ROLE_PERMISSION.PERMISSION_ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    private Integer permissionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ROLE_PERMISSION.GMT_CREATE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    private Long gmtCreate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ROLE_PERMISSION.ID
     *
     * @return the value of ROLE_PERMISSION.ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ROLE_PERMISSION.ID
     *
     * @param id the value for ROLE_PERMISSION.ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ROLE_PERMISSION.ROLE_ID
     *
     * @return the value of ROLE_PERMISSION.ROLE_ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ROLE_PERMISSION.ROLE_ID
     *
     * @param roleId the value for ROLE_PERMISSION.ROLE_ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ROLE_PERMISSION.PERMISSION_ID
     *
     * @return the value of ROLE_PERMISSION.PERMISSION_ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public Integer getPermissionId() {
        return permissionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ROLE_PERMISSION.PERMISSION_ID
     *
     * @param permissionId the value for ROLE_PERMISSION.PERMISSION_ID
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ROLE_PERMISSION.GMT_CREATE
     *
     * @return the value of ROLE_PERMISSION.GMT_CREATE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ROLE_PERMISSION.GMT_CREATE
     *
     * @param gmtCreate the value for ROLE_PERMISSION.GMT_CREATE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}