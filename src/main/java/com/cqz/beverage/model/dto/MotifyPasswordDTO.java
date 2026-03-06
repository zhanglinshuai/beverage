package com.cqz.beverage.model.dto;

import java.io.Serializable;

/**
 * 修改密码的响应参数
 */
public class MotifyPasswordDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean isMotified;
    private String motifiedPassword;
}
