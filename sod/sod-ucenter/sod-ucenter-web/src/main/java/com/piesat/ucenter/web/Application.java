package com.piesat.ucenter.web;
import com.piesat.common.grpc.annotation.GrpcServiceScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.piesat.*"})
@GrpcServiceScan(packages = {"com.piesat"})
@ServletComponentScan(basePackages={"com.piesat.*"})
@Slf4j
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class,args);
		log.info("项目启动启动成功！访问地址: http://localhost:1234");
	}
}
