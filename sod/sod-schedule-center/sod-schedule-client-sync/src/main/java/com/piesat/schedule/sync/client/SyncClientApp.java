package com.piesat.schedule.sync.client;

import com.piesat.common.grpc.annotation.GrpcServiceScan;
import com.piesat.common.jpa.dao.GenericDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

/**
 * @author cwh
 * @date 2020年 10月23日 17:58:30
 */
@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
@EnableAutoConfiguration(exclude = {RedisRepositoriesAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@ComponentScan(basePackages = {"com.piesat.*"})
@GrpcServiceScan(packages = {"com.piesat"})
@EnableJpaRepositories(basePackages = { "com.piesat" },repositoryBaseClass = GenericDaoImpl.class)
@EnableJpaAuditing
@EntityScan(basePackages = { "com.piesat" })
@MapperScan("com.piesat.*.mapper")
@Slf4j
public class SyncClientApp {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(SyncClientApp.class,args);
    }
}
