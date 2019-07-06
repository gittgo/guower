package com.ourslook.guower.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审核结果表
 */
public class ExamineCheck implements Serializable {

    /**
     * 审核对象id数组
     */
    private Integer[] ids;

    /**
     * 审核结果
     */
    private Integer result;

    /**
     * 审核系统人员id
     */
    private Integer sysUserId;

    /**
     * 审核时间
     */
    private LocalDateTime examineDate;

    /**
     * 审核备注
     */
    private String remarks;

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    public LocalDateTime getExamineDate() {
        return examineDate;
    }

    public void setExamineDate(LocalDateTime examineDate) {
        this.examineDate = examineDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
