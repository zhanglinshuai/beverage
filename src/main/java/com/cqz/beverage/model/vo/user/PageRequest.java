package com.cqz.beverage.model.vo.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 当前页码
     */
    private  int pageNum = 1;
    /**
     * 每页条数
     */
    private  int pageSize = 10;

    private Integer status;
}
