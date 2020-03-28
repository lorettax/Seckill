package com.miaosha.dao;

import com.miaosha.dataobject.SequenceDo;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Wed Mar 18 23:37:31 CST 2020
     */
    int deleteByPrimaryKey(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Wed Mar 18 23:37:31 CST 2020
     */
    int insert(SequenceDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Wed Mar 18 23:37:31 CST 2020
     */
    int insertSelective(SequenceDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Wed Mar 18 23:37:31 CST 2020
     */
    SequenceDo selectByPrimaryKey(String name);

    SequenceDo getSequenceByName(String name);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Wed Mar 18 23:37:31 CST 2020
     */
    int updateByPrimaryKeySelective(SequenceDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sequence_info
     *
     * @mbg.generated Wed Mar 18 23:37:31 CST 2020
     */
    int updateByPrimaryKey(SequenceDo record);
}