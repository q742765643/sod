package com.piesat.monitor

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.{EnableAutoConfiguration, SpringBootApplication}
import org.springframework.context.annotation.ComponentScan

/**
  * Created by zzj on 2020/3/19.
  */
//@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan (basePackages =  Array ("com.piesat.*")  )
class Config

object ScalaSpringApplication extends App {
  SpringApplication.run(classOf[Config])
}
