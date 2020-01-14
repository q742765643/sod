package com.piesat.sod.system.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.piesat.common.grpc.annotation.GrpcServiceScan;
import com.piesat.common.jpa.dao.GenericDaoImpl;
/**
 * 存储管理系统 _ 系统管理相关模块
 * @description
 * @author wlg
 * @date 2019-11-20 10:53
 */
@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.piesat.*"})
@GrpcServiceScan(packages = {"com.piesat"})
//@ServletComponentScan(basePackages={"com.piesat.*"})
@EnableJpaRepositories(basePackages = { "com.piesat" },repositoryBaseClass = GenericDaoImpl.class)
@EnableJpaAuditing
@EntityScan(basePackages = { "com.piesat" })
@MapperScan("com.piesat.sod.*.mapper")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
	}
}
