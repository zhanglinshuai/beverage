package com.cqz.beverage.model.vo.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 根据指定条件来搜索用户
 */
@Data
public class SearchUserRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String keyword;
    private PageRequest pageRequest;
}
