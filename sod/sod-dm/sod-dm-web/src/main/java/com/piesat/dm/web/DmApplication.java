package com.piesat.dm.web;

import com.piesat.common.grpc.annotation.GrpcServiceScan;
import com.piesat.common.jpa.dao.GenericDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.piesat.*"})
@GrpcServiceScan(packages = {"com.piesat"})
//@ServletComponentScan(basePackages={"com.piesat.*"})
@EnableJpaRepositories(basePackages = { "com.piesat" },repositoryBaseClass = GenericDaoImpl.class)
@EnableJpaAuditing
@EntityScan(basePackages = { "com.piesat" })
@MapperScan("com.piesat.*.mapper")
@Slf4j
public class DmApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmApplication.class, args);
    }

}
