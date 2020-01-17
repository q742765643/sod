package com.piesat.dm.core.parser;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 数据库支持类型
 *
 * @author cwh
 * @date 2019年 12月27日 18:35:13
 */
@Data
@Component
@PropertySource(value = {"classpath:config.yml"}, factory = YamlConfigFactory.class)
@ConfigurationProperties(prefix = "column")
public class ColumnSet {
    private Map<String, Object> xugu;
    private Map<String, Object> gbase8a;
    private Map<String, Object> cassandra;
}
