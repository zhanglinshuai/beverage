package com.cqz.beverage.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分页响应
 */
@Data
public class PageResponseDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long total;
    private Long pages;
    private int pageNum;
    private int pageSize;
    private List<T> records;

    public static <T> PageResponseDTO<T> buildPageResponseDTO(Page<T> page) {
        PageResponseDTO<T> dto = new PageResponseDTO<>();
        dto.setTotal(page.getTotal());
        dto.setPages(page.getPages());
        dto.setPageNum((int) page.getCurrent());
        dto.setPageSize((int) page.getSize());
        dto.setRecords(page.getRecords());
        return dto;
    }
}
