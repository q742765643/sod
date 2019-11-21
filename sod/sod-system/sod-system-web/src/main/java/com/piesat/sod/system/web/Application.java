package com.piesat.sod.system.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.piesat.common.grpc.annotation.GrpcServiceScan;
/**
 * 存储管理系统 _ 系统管理相关模块
 * @description
 * @author wlg
 * @date 2019-11-20 10:53
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.piesat.*"})
@GrpcServiceScan(packages = {"com.piesat"})
@ServletComponentScan(basePackages={"com.piesat.*"})
@EnableJpaRepositories(basePackages = { "com.piesat" })
@EntityScan(basePackages = { "com.piesat" })
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
	}
}
