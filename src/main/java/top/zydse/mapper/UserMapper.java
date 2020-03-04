package top.zydse.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.zydse.model.User;

import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: UserMapper
 * Description:
 *
 * @Date: 2020/3/4
 */
@Repository
@Mapper
public interface UserMapper {
    @Select("select * from user")
    List<User> findAll();

    @Select("select * from user where id = #{uid}")
    User findById(Integer uid);

    @Select("select * from user where token = #{token}")
    User findByToken(String token);

    @Insert("insert into user(account_id, name, token, gmt_create, gmt_modified)" +
            " values (#{account_id}, #{name}, #{token}, #{gmt_create}, #{gmt_modified})")
    void saveUser(User user);

    @Update("update user set account_id=#{account_id}, name=#{name}, token=#{token}," +
            " gmt_create=#{gmt_create}, gmt_modified=#{gmt_modified} where id=#{id}")
    void updateUser(User user);

    @Delete("delete from user where id=#{id}")
    void deleteUser(User user);

    @Delete("delete from user where id=#{id}")
    void deleteUserById(Integer id);



}
