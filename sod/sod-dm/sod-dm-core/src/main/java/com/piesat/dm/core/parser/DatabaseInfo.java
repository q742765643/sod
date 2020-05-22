package com.piesat.dm.core.parser;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 数据库信息
 *
 * @author cwh
 * @date 2020年 02月10日 09:42:07
 */
@Data
@Component
@PropertySource(value = {"classpath:config.yml"}, factory = YamlConfigFactory.class)
@ConfigurationProperties(prefix = "databasename")
public class DatabaseInfo {
    private String xugu;
    private String gbase8a;
    private String cassandra;
    private String postgresql;
}
