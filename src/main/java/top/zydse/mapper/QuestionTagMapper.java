package top.zydse.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import top.zydse.model.QuestionTag;
import top.zydse.model.QuestionTagExample;

public interface QuestionTagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    long countByExample(QuestionTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int deleteByExample(QuestionTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int insert(QuestionTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int insertSelective(QuestionTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    List<QuestionTag> selectByExampleWithRowbounds(QuestionTagExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    List<QuestionTag> selectByExample(QuestionTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    QuestionTag selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int updateByExampleSelective(@Param("record") QuestionTag record, @Param("example") QuestionTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int updateByExample(@Param("record") QuestionTag record, @Param("example") QuestionTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int updateByPrimaryKeySelective(QuestionTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QUESTION_TAG
     *
     * @mbg.generated Sat Mar 28 21:45:12 CST 2020
     */
    int updateByPrimaryKey(QuestionTag record);
}