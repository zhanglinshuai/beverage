package com.cqz.beverage.constant;

/**
 * jwt中用到的常量
 */
public interface JwtConstant {
    long EXPIRATION = 1000 * 60 * 60 * 24;
    String SECRET = "8k9S7#2p8Q9$5m7B3v8N6&4F9g7H2j8K9%3s7D8f9G7h6J8k";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER = "Authorization";
}
