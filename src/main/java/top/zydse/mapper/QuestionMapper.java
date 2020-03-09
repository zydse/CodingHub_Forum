package top.zydse.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.zydse.model.Question;

import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: QuestionMapper
 * Description:
 *
 * @Date: 2020/3/4
 */
@Repository
@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title, description, creator, tags, gmt_create, gmt_modified)" +
            " values (#{title}, #{description}, #{creator}, #{tags}, #{gmtCreate}, #{gmtModified})")
    void insert(Question question);

    @Select("select * from question limit #{offset}, #{size};")
    List<Question> findAll(Integer offset, Integer size);

    @Select("select count(1) from question")
    Integer findTotal();
}
