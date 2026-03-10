package com.cqz.beverage.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 库存变更记录
 * @TableName change_record
 */
@TableName(value ="change_record")
@Data
public class ChangeRecord {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备编号
     */
    @TableField(value = "device_code")
    private String deviceCode;

    /**
     * 商品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 品牌
     */
    @TableField(value = "brand")
    private String brand;

    /**
     * 货道编号
     */
    @TableField(value = "channel_no")
    private String channelNo;

    /**
     * 操作类型
     */
    @TableField(value = "operation_type")
    private String operationType;

    /**
     * 变更数量
     */
    @TableField(value = "change_amount")
    private Integer changeAmount;

    /**
     * 操作人
     */
    @TableField(value = "operator_name")
    private String operatorName;

    /**
     * 变更时间
     */
    @TableField(value = "change_time")
    private Date changeTime;

    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(value = "description")
    private String description;


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
        ChangeRecord other = (ChangeRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceCode() == null ? other.getDeviceCode() == null : this.getDeviceCode().equals(other.getDeviceCode()))
            && (this.getProductName() == null ? other.getProductName() == null : this.getProductName().equals(other.getProductName()))
            && (this.getBrand() == null ? other.getBrand() == null : this.getBrand().equals(other.getBrand()))
            && (this.getChannelNo() == null ? other.getChannelNo() == null : this.getChannelNo().equals(other.getChannelNo()))
            && (this.getOperationType() == null ? other.getOperationType() == null : this.getOperationType().equals(other.getOperationType()))
            && (this.getChangeAmount() == null ? other.getChangeAmount() == null : this.getChangeAmount().equals(other.getChangeAmount()))
            && (this.getOperatorName() == null ? other.getOperatorName() == null : this.getOperatorName().equals(other.getOperatorName()))
            && (this.getChangeTime() == null ? other.getChangeTime() == null : this.getChangeTime().equals(other.getChangeTime()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceCode() == null) ? 0 : getDeviceCode().hashCode());
        result = prime * result + ((getProductName() == null) ? 0 : getProductName().hashCode());
        result = prime * result + ((getBrand() == null) ? 0 : getBrand().hashCode());
        result = prime * result + ((getChannelNo() == null) ? 0 : getChannelNo().hashCode());
        result = prime * result + ((getOperationType() == null) ? 0 : getOperationType().hashCode());
        result = prime * result + ((getChangeAmount() == null) ? 0 : getChangeAmount().hashCode());
        result = prime * result + ((getOperatorName() == null) ? 0 : getOperatorName().hashCode());
        result = prime * result + ((getChangeTime() == null) ? 0 : getChangeTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deviceCode=").append(deviceCode);
        sb.append(", productName=").append(productName);
        sb.append(", brand=").append(brand);
        sb.append(", channelNo=").append(channelNo);
        sb.append(", operationType=").append(operationType);
        sb.append(", changeAmount=").append(changeAmount);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", changeTime=").append(changeTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", description=").append(description);
        sb.append("]");
        return sb.toString();
    }
}