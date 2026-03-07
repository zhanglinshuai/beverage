package com.cqz.beverage.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 设备档案记录表
 * @TableName device_archive
 */
@TableName(value ="device_archive")
@Data
public class DeviceArchive {
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
     * 维护类型
     */
    @TableField(value = "maintenance_type")
    private String maintenanceType;

    /**
     * 维护内容
     */
    @TableField(value = "maintenance_content")
    private String maintenanceContent;

    /**
     * 维护时间
     */
    @TableField(value = "maintenance_time")
    private Date maintenanceTime;

    /**
     * 维护人员
     */
    @TableField(value = "operator")
    private String operator;

    /**
     * 生产厂家
     */
    @TableField(value = "manufacturer")
    private String manufacturer;

    /**
     * 生产日期
     */
    @TableField(value = "production_date")
    private Date productionDate;

    /**
     * 安装日期
     */
    @TableField(value = "install_date")
    private Date installDate;

    /**
     * 保修期限
     */
    @TableField(value = "warranty_period")
    private String warrantyPeriod;

    /**
     * 最后维修时间
     */
    @TableField(value = "last_maintenance_time")
    private Date lastMaintenanceTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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
        DeviceArchive other = (DeviceArchive) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceCode() == null ? other.getDeviceCode() == null : this.getDeviceCode().equals(other.getDeviceCode()))
            && (this.getMaintenanceType() == null ? other.getMaintenanceType() == null : this.getMaintenanceType().equals(other.getMaintenanceType()))
            && (this.getMaintenanceContent() == null ? other.getMaintenanceContent() == null : this.getMaintenanceContent().equals(other.getMaintenanceContent()))
            && (this.getMaintenanceTime() == null ? other.getMaintenanceTime() == null : this.getMaintenanceTime().equals(other.getMaintenanceTime()))
            && (this.getOperator() == null ? other.getOperator() == null : this.getOperator().equals(other.getOperator()))
            && (this.getManufacturer() == null ? other.getManufacturer() == null : this.getManufacturer().equals(other.getManufacturer()))
            && (this.getProductionDate() == null ? other.getProductionDate() == null : this.getProductionDate().equals(other.getProductionDate()))
            && (this.getInstallDate() == null ? other.getInstallDate() == null : this.getInstallDate().equals(other.getInstallDate()))
            && (this.getWarrantyPeriod() == null ? other.getWarrantyPeriod() == null : this.getWarrantyPeriod().equals(other.getWarrantyPeriod()))
            && (this.getLastMaintenanceTime() == null ? other.getLastMaintenanceTime() == null : this.getLastMaintenanceTime().equals(other.getLastMaintenanceTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceCode() == null) ? 0 : getDeviceCode().hashCode());
        result = prime * result + ((getMaintenanceType() == null) ? 0 : getMaintenanceType().hashCode());
        result = prime * result + ((getMaintenanceContent() == null) ? 0 : getMaintenanceContent().hashCode());
        result = prime * result + ((getMaintenanceTime() == null) ? 0 : getMaintenanceTime().hashCode());
        result = prime * result + ((getOperator() == null) ? 0 : getOperator().hashCode());
        result = prime * result + ((getManufacturer() == null) ? 0 : getManufacturer().hashCode());
        result = prime * result + ((getProductionDate() == null) ? 0 : getProductionDate().hashCode());
        result = prime * result + ((getInstallDate() == null) ? 0 : getInstallDate().hashCode());
        result = prime * result + ((getWarrantyPeriod() == null) ? 0 : getWarrantyPeriod().hashCode());
        result = prime * result + ((getLastMaintenanceTime() == null) ? 0 : getLastMaintenanceTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
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
        sb.append(", maintenanceType=").append(maintenanceType);
        sb.append(", maintenanceContent=").append(maintenanceContent);
        sb.append(", maintenanceTime=").append(maintenanceTime);
        sb.append(", operator=").append(operator);
        sb.append(", manufacturer=").append(manufacturer);
        sb.append(", productionDate=").append(productionDate);
        sb.append(", installDate=").append(installDate);
        sb.append(", warrantyPeriod=").append(warrantyPeriod);
        sb.append(", lastMaintenanceTime=").append(lastMaintenanceTime);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}