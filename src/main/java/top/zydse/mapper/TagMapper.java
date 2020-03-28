package top.zydse.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import top.zydse.model.Tag;
import top.zydse.model.TagExample;

public interface TagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    long countByExample(TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int deleteByExample(TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int insert(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int insertSelective(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    List<Tag> selectByExampleWithRowbounds(TagExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    List<Tag> selectByExample(TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    Tag selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int updateByExampleSelective(@Param("record") Tag record, @Param("example") TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int updateByExample(@Param("record") Tag record, @Param("example") TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int updateByPrimaryKeySelective(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int updateByPrimaryKey(Tag record);
}