package top.zydse.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import top.zydse.model.Permission;
import top.zydse.model.PermissionExample;

public interface PermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    long countByExample(PermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int deleteByExample(PermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int insert(Permission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int insertSelective(Permission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    List<Permission> selectByExampleWithRowbounds(PermissionExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    List<Permission> selectByExample(PermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    Permission selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByExampleSelective(@Param("record") Permission record, @Param("example") PermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByExample(@Param("record") Permission record, @Param("example") PermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByPrimaryKeySelective(Permission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PERMISSION
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByPrimaryKey(Permission record);
}