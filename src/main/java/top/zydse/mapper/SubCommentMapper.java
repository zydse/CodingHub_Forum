package top.zydse.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import top.zydse.model.SubComment;
import top.zydse.model.SubCommentExample;

public interface SubCommentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    long countByExample(SubCommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int deleteByExample(SubCommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int insert(SubComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int insertSelective(SubComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    List<SubComment> selectByExampleWithRowbounds(SubCommentExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    List<SubComment> selectByExample(SubCommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    SubComment selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByExampleSelective(@Param("record") SubComment record, @Param("example") SubCommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByExample(@Param("record") SubComment record, @Param("example") SubCommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByPrimaryKeySelective(SubComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SUB_COMMENT
     *
     * @mbg.generated Fri Apr 17 20:16:03 CST 2020
     */
    int updateByPrimaryKey(SubComment record);
}