package com.piesat.dao.mapper;

import com.piesat.model.mybatis.TSodJobInfoLog;

public interface TSodJobInfoLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_SOD_JOB_INFO_LOG
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_SOD_JOB_INFO_LOG
     *
     * @mbg.generated
     */
    int insert(TSodJobInfoLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_SOD_JOB_INFO_LOG
     *
     * @mbg.generated
     */
    int insertSelective(TSodJobInfoLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_SOD_JOB_INFO_LOG
     *
     * @mbg.generated
     */
    TSodJobInfoLog selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_SOD_JOB_INFO_LOG
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(TSodJobInfoLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_SOD_JOB_INFO_LOG
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(TSodJobInfoLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_SOD_JOB_INFO_LOG
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(TSodJobInfoLog record);
}