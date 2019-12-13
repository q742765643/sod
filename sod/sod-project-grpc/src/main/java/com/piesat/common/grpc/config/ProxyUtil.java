package com.piesat.common.grpc.config;

import com.google.common.collect.Lists;
import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.binding.GrpcServiceProxy;
import com.piesat.common.grpc.util.ClassNameUtils;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.stub.AbstractStub;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelFactory;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.StubTransformer;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/13 13:12
 */
@Slf4j
public class ProxyUtil implements ApplicationContextAware {
    public static ConcurrentHashMap<String,Object> grpcServices=new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Channel> grpcChannel=new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> grpcServerName=new ConcurrentHashMap<>();
    private static ApplicationContext applicationContext;
    private GrpcChannelFactory channelFactory = null;
    private List<StubTransformer> stubTransformers = null;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(ProxyUtil.applicationContext == null) {
            ProxyUtil.applicationContext = applicationContext;
        }
        System.out.println("---------------------------------------------------------------------");

    }

    static void registerBeans(BeanFactory beanFactory, Set<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String className = beanDefinition.getBeanClassName();
            if (StringUtils.isEmpty(className)) {
                continue;
            }
            try {
                // 创建代理类
                Class<?> target = Class.forName(className);
                Object invoker = new Object();
                InvocationHandler invocationHandler = new GrpcServiceProxy<>(target, invoker);
                Object proxy = Proxy.newProxyInstance(GrpcHthtService.class.getClassLoader(), new Class[]{target}, invocationHandler);

                // 注册到 Spring 容器
                String beanName = ClassNameUtils.beanName(className);
                grpcServices.put(className,proxy);
                //((DefaultListableBeanFactory) beanFactory).registerSingleton(beanName+"GPRC", proxy);
            } catch (ClassNotFoundException e) {
                log.warn("class not found : " + className);
            }
        }
    }
    private void getgrpcChannel(String className, GrpcClient annotation){
        String name=annotation.value();
        GrpcAutoConfiguration.ProxyUtil.grpcServerName.put(className,name);
        if(null==GrpcAutoConfiguration.ProxyUtil.grpcChannel.get(name)){
            Channel channel= processInjectionPoint(null, Channel.class, annotation);
            if(null!=channel){
                GrpcAutoConfiguration.ProxyUtil.grpcChannel.put(name,channel);
            }else {
                log.error("通道初始化失败{}",name);
            }
        }
    }
    protected <T> T processInjectionPoint(final Member injectionTarget, final Class<T> injectionType,
                                          final GrpcClient annotation) {
        final List<ClientInterceptor> interceptors = interceptorsFromAnnotation(annotation);
        final String name = annotation.value();
        final Channel channel;
        try {
            channel = getChannelFactory().createChannel(name, interceptors, annotation.sortInterceptors());
            if (channel == null) {
                throw new IllegalStateException("Channel factory created a null channel for " + name);
            }
        } catch (final RuntimeException e) {
            throw new IllegalStateException("Failed to create channel: " + name, e);
        }

        final T value = valueForMember(name, injectionTarget, injectionType, channel);
        if (value == null) {
            throw new IllegalStateException(
                    "Injection value is null unexpectedly for " + name + " at " + injectionTarget);
        }
        return value;
    }
    protected List<ClientInterceptor> interceptorsFromAnnotation(final GrpcClient annotation) throws BeansException {
        final List<ClientInterceptor> list = Lists.newArrayList();
        for (final Class<? extends ClientInterceptor> interceptorClass : annotation.interceptors()) {
            final ClientInterceptor clientInterceptor;
            if (this.applicationContext.getBeanNamesForType(ClientInterceptor.class).length > 0) {
                clientInterceptor = this.applicationContext.getBean(interceptorClass);
            } else {
                try {
                    clientInterceptor = interceptorClass.getConstructor().newInstance();
                } catch (final Exception e) {
                    throw new BeanCreationException("Failed to create interceptor instance", e);
                }
            }
            list.add(clientInterceptor);
        }
        for (final String interceptorName : annotation.interceptorNames()) {
            list.add(this.applicationContext.getBean(interceptorName, ClientInterceptor.class));
        }
        return list;
    }

    private GrpcChannelFactory getChannelFactory() {
        if (this.channelFactory == null) {
            final GrpcChannelFactory factory = this.applicationContext.getBean(GrpcChannelFactory.class);
            this.channelFactory = factory;
            return factory;
        }
        return this.channelFactory;
    }
    protected <T> T valueForMember(final String name, final Member injectionTarget, final Class<T> injectionType,
                                   final Channel channel) throws BeansException {
        if (Channel.class.equals(injectionType)) {
            return injectionType.cast(channel);
        } else if (AbstractStub.class.isAssignableFrom(injectionType)) {
            try {
                @SuppressWarnings("unchecked")
                final Class<? extends AbstractStub<?>> stubClass =
                        (Class<? extends AbstractStub<?>>) injectionType.asSubclass(AbstractStub.class);
                final Constructor<? extends AbstractStub<?>> constructor =
                        ReflectionUtils.accessibleConstructor(stubClass, Channel.class);
                AbstractStub<?> stub = constructor.newInstance(channel);
                for (final StubTransformer stubTransformer : getStubTransformers()) {
                    stub = stubTransformer.transform(name, stub);
                }
                return injectionType.cast(stub);
            } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
                throw new BeanInstantiationException(injectionType,
                        "Failed to create gRPC client for : " + injectionTarget, e);
            }
        } else {
            throw new InvalidPropertyException(injectionTarget.getDeclaringClass(), injectionTarget.getName(),
                    "Unsupported type " + injectionType.getName());
        }
    }

    private List<StubTransformer> getStubTransformers() {
        if (this.stubTransformers == null) {
            final Collection<StubTransformer> transformers =
                    this.applicationContext.getBeansOfType(StubTransformer.class).values();
            this.stubTransformers = new ArrayList<>(transformers);
            return this.stubTransformers;
        }
        return this.stubTransformers;
    }
}
