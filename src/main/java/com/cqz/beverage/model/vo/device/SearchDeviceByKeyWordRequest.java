package com.cqz.beverage.model.vo.device;

import com.cqz.beverage.model.vo.user.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 根据关键词来搜索设备请求参数
 */
@Data
public class SearchDeviceByKeyWordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String Keyword;
    private PageRequest pageRequest;
}
