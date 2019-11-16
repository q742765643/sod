package com.piesat.common.grpc.config;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/13 16:10
 */
@Component
public class ServiceInjectProcessor implements BeanPostProcessor {
    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targeClass = bean.getClass();
        Field[] fields = targeClass.getDeclaredFields();
        for (Field field: fields ) {
            if (field.isAnnotationPresent(GrpcHthtClient.class)) {  //判断属性是否是自定义注解@MyAnnotation
                if(!field.getType().isInterface()) {  //加自定义注解的属性必须是接口类型（这样才可能出现多个不同的实例bean)
                    throw new BeanCreationException("GrpcHthtClient field must be declared an interface");
                } else {
                    try {
                        this.hanldGrpcHthtClient(field, bean, field.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return bean;
    }
    private void hanldGrpcHthtClient(Field field, Object bean, Class type) throws IllegalAccessException {
        //获取所有该属性接口的实例bean
        Object o= GrpcAutoConfiguration.ProxyUtil.grpcServices.get(field.getType().getName());
        //设置该域可设置修改
        field.setAccessible(true);
        //获取注解@MyAnnotation中配置的value值
        //String injectVal = field.getAnnotation(GrpcHthtClient.class).;
        //将找到的实例赋值给属性域
        field.set(bean,o);
    }

}
