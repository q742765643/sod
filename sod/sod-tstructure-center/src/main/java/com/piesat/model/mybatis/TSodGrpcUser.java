package com.piesat.model.mybatis;

import java.util.Date;

public class TSodGrpcUser {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_SOD_GRPC_USER.ID
     *
     * @mbg.generated
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_SOD_GRPC_USER.CREATE_TIME
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_SOD_GRPC_USER.GPRC_PASSWORD
     *
     * @mbg.generated
     */
    private String gprcPassword;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_SOD_GRPC_USER.GRPC_USER
     *
     * @mbg.generated
     */
    private String grpcUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_SOD_GRPC_USER.UPDATE_TIME
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_SOD_GRPC_USER.ID
     *
     * @return the value of T_SOD_GRPC_USER.ID
     *
     * @mbg.generated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_SOD_GRPC_USER.ID
     *
     * @param id the value for T_SOD_GRPC_USER.ID
     *
     * @mbg.generated
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_SOD_GRPC_USER.CREATE_TIME
     *
     * @return the value of T_SOD_GRPC_USER.CREATE_TIME
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_SOD_GRPC_USER.CREATE_TIME
     *
     * @param createTime the value for T_SOD_GRPC_USER.CREATE_TIME
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_SOD_GRPC_USER.GPRC_PASSWORD
     *
     * @return the value of T_SOD_GRPC_USER.GPRC_PASSWORD
     *
     * @mbg.generated
     */
    public String getGprcPassword() {
        return gprcPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_SOD_GRPC_USER.GPRC_PASSWORD
     *
     * @param gprcPassword the value for T_SOD_GRPC_USER.GPRC_PASSWORD
     *
     * @mbg.generated
     */
    public void setGprcPassword(String gprcPassword) {
        this.gprcPassword = gprcPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_SOD_GRPC_USER.GRPC_USER
     *
     * @return the value of T_SOD_GRPC_USER.GRPC_USER
     *
     * @mbg.generated
     */
    public String getGrpcUser() {
        return grpcUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_SOD_GRPC_USER.GRPC_USER
     *
     * @param grpcUser the value for T_SOD_GRPC_USER.GRPC_USER
     *
     * @mbg.generated
     */
    public void setGrpcUser(String grpcUser) {
        this.grpcUser = grpcUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_SOD_GRPC_USER.UPDATE_TIME
     *
     * @return the value of T_SOD_GRPC_USER.UPDATE_TIME
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_SOD_GRPC_USER.UPDATE_TIME
     *
     * @param updateTime the value for T_SOD_GRPC_USER.UPDATE_TIME
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}