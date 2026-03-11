package com.cqz.beverage.model.dto.workOrder;

import com.cqz.beverage.model.WorkOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 工单的响应信息
 */
@Data
public class WorkOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String workNo;
    private String deviceCode;
    private String operatorName;
    private String maintainerName;
    private String workType;
    private String workStatus;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime finishTime;

    public static WorkOrderDTO fromEntity(WorkOrder entity){
        WorkOrderDTO dto = new WorkOrderDTO();
        dto.setWorkNo(entity.getWorkNo());
        dto.setWorkType(entity.getWorkType());
        dto.setWorkStatus(entity.getWorkStatus());
        dto.setDescription(entity.getDescription());
        dto.setCreateTime(entity.getCreateTime());
        dto.setFinishTime(entity.getFinishTime());
        return dto;
    }
}
