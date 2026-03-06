package com.cqz.beverage.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 工单表
 * @TableName work_order
 */
@TableName(value ="work_order")
@Data
public class WorkOrder {
    /**
     * 工单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工单编号
     */
    @TableField(value = "work_no")
    private String workNo;

    /**
     * 设备id
     */
    @TableField(value = "device_id")
    private Long deviceId;

    /**
     * 运营商id
     */
    @TableField(value = "operator_id")
    private Long operatorId;

    /**
     * 运维人员id
     */
    @TableField(value = "maintainer_id")
    private Long maintainerId;

    /**
     * 工单类型
     */
    @TableField(value = "work_type")
    private String workType;

    /**
     * 工单状态
     */
    @TableField(value = "work_status")
    private String workStatus;

    /**
     * 工单问题描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 完成时间
     */
    @TableField(value = "finish_time")
    private Date finishTime;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        WorkOrder other = (WorkOrder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getWorkNo() == null ? other.getWorkNo() == null : this.getWorkNo().equals(other.getWorkNo()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
            && (this.getMaintainerId() == null ? other.getMaintainerId() == null : this.getMaintainerId().equals(other.getMaintainerId()))
            && (this.getWorkType() == null ? other.getWorkType() == null : this.getWorkType().equals(other.getWorkType()))
            && (this.getWorkStatus() == null ? other.getWorkStatus() == null : this.getWorkStatus().equals(other.getWorkStatus()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getFinishTime() == null ? other.getFinishTime() == null : this.getFinishTime().equals(other.getFinishTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWorkNo() == null) ? 0 : getWorkNo().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getMaintainerId() == null) ? 0 : getMaintainerId().hashCode());
        result = prime * result + ((getWorkType() == null) ? 0 : getWorkType().hashCode());
        result = prime * result + ((getWorkStatus() == null) ? 0 : getWorkStatus().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getFinishTime() == null) ? 0 : getFinishTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", workNo=").append(workNo);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", maintainerId=").append(maintainerId);
        sb.append(", workType=").append(workType);
        sb.append(", workStatus=").append(workStatus);
        sb.append(", description=").append(description);
        sb.append(", createTime=").append(createTime);
        sb.append(", finishTime=").append(finishTime);
        sb.append("]");
        return sb.toString();
    }
}