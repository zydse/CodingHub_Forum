package top.zydse.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import top.zydse.model.UserRole;
import top.zydse.model.UserRoleExample;

public interface UserRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    long countByExample(UserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int deleteByExample(UserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int insert(UserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int insertSelective(UserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    List<UserRole> selectByExampleWithRowbounds(UserRoleExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    List<UserRole> selectByExample(UserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    UserRole selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByExampleSelective(@Param("record") UserRole record, @Param("example") UserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByExample(@Param("record") UserRole record, @Param("example") UserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByPrimaryKeySelective(UserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table USER_ROLE
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByPrimaryKey(UserRole record);
}