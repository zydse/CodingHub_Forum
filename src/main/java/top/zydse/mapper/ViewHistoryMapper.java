package top.zydse.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import top.zydse.model.ViewHistory;
import top.zydse.model.ViewHistoryExample;

public interface ViewHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    long countByExample(ViewHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    int deleteByExample(ViewHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    int insert(ViewHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    int insertSelective(ViewHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    List<ViewHistory> selectByExampleWithRowbounds(ViewHistoryExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    List<ViewHistory> selectByExample(ViewHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    ViewHistory selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    int updateByExampleSelective(@Param("record") ViewHistory record, @Param("example") ViewHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    int updateByExample(@Param("record") ViewHistory record, @Param("example") ViewHistoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    int updateByPrimaryKeySelective(ViewHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table VIEW_HISTORY
     *
     * @mbg.generated Tue Apr 14 20:29:00 CST 2020
     */
    int updateByPrimaryKey(ViewHistory record);
}