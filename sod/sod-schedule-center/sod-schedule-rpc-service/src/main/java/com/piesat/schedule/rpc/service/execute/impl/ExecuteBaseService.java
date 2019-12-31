package com.piesat.schedule.rpc.service.execute.impl;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.Application;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.grpc.constant.GrpcResponseStatus;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.common.grpc.exception.GrpcException;
import com.piesat.common.grpc.service.GrpcClientService;
import com.piesat.common.grpc.service.GrpcRequest;
import com.piesat.common.grpc.service.GrpcResponse;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.rpc.proxy.GrpcServiceProxy;
import com.piesat.schedule.rpc.vo.Server;
import com.piesat.sso.client.util.RedisUtil;
import com.piesat.util.ResultT;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 17:47
 */
@Slf4j
@Service("executeBaseService")
public abstract class ExecuteBaseService {
      private static final String QUARTZ_HTHT_PERFORM="QUARTZ:HTHT:PERFORM";
      @Autowired
      private RedisUtil redisUtil;

      public  void executeBusiness(JobInfoEntity jobInfoEntity, ResultT<String> resultT){
            List<Server> servers=this.findServer();
            if(servers.size()==0){
                  resultT.setCode(301);
                  return;
            }
            Server server=this.operationalControl(jobInfoEntity,servers,resultT);
            if(server==null){
                  resultT.setCode(301);
                  return;
            }
            this.remote(jobInfoEntity,server,resultT);
      }
      public abstract Server operationalControl(JobInfoEntity jobInfoEntity,List<Server> servers,ResultT<String> resultT);


      public List<Server> findServer(){
            List<Server> servers=new ArrayList<>();
            DiscoveryClient discoveryClient= SpringUtil.getBean(DiscoveryClient.class);
            Application application=discoveryClient.getApplication("schedule-client-server");
            List<InstanceInfo> instanceInfos=application.getInstances();
            if(instanceInfos==null||instanceInfos.size()==0){
                  return servers;
            }
            for(InstanceInfo instanceInfo:instanceInfos){
                 if(InstanceInfo.InstanceStatus.UP==instanceInfo.getStatus()){
                       Server server=new Server();
                       server.setHost(instanceInfo.getIPAddr());
                       server.setHttpPort(instanceInfo.getPort());
                       server.setGrpcPort(Integer.valueOf(instanceInfo.getMetadata().get("gRPC.port")));
                       long count=redisUtil.scanSize(QUARTZ_HTHT_PERFORM+":"+server.getHost()+":"+server.getGrpcPort());
                       if(count<server.getLimit()){
                             servers.add(server);
                       }
                 }
            }
            return servers;
      }

      public void remote(JobInfoEntity jobInfoEntity,Server server,ResultT<String> resultT){
            try {
                  Class<?> target = ExecutorBiz.class;
                  Object invoker = new Object();
                  InvocationHandler invocationHandler = new GrpcServiceProxy<>(target, invoker,server);
                  ExecutorBiz executorBiz = (ExecutorBiz) Proxy.newProxyInstance(GrpcHthtService.class.getClassLoader(), new Class[]{target}, invocationHandler);
                  executorBiz.execute(jobInfoEntity);
            } catch (Exception e) {
                  resultT.setCode(302);
                  e.printStackTrace();
            }
      }


}
