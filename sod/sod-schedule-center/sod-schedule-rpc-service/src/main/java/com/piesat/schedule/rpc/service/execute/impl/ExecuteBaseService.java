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
import com.piesat.schedule.enums.ExecutorBlockStrategyEnum;
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
import java.util.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 17:47
 */
@Slf4j
@Service("executeBaseService")
public abstract class ExecuteBaseService {
      protected static final String QUARTZ_HTHT_PERFORM="QUARTZ:HTHT:PERFORM";
      protected static final String QUARTZ_HTHT_TASK_SERIAL="QUARTZ:HTHT:SINGLE:SERIAL";
      protected static final String QUARTZ_HTHT_CLUSTER_SERIAL="QUARTZ:HTHT:CLUSTER:SERIAL";

      @Autowired
      private RedisUtil redisUtil;

      public  void executeBusiness(JobInfoEntity jobInfoEntity, ResultT<String> resultT){
            List<Server> servers=this.findServer(jobInfoEntity);

            Server server=this.operationalControl(jobInfoEntity,servers,resultT);
            if(server==null){
                  resultT.setCode(301);
                  return;
            }
            this.remote(jobInfoEntity,server,resultT);
      }
      public  Server operationalControl(JobInfoEntity jobInfoEntity,List<Server> servers,ResultT<String> resultT){
            if(servers.size()==0){
                  resultT.setCode(301);
                  return null;
            }
            Collections.sort(servers, new Comparator<Server>() {

                  @Override
                  public int compare(Server o1, Server o2) {
                     float a=o1.getUse()/o1.getLimit();
                     float b=o2.getUse()/o2.getLimit();
                     if(a>b){
                           return 1;
                     }
                     if(a==b){
                           return 0;
                     }else{
                           return -1;
                     }

                  }
            });
            return servers.get(0);
      }


      public List<Server> findServer(JobInfoEntity jobInfoEntity){
            List<Server> servers=new ArrayList<>();
            DiscoveryClient discoveryClient= SpringUtil.getBean(DiscoveryClient.class);
            Application application=discoveryClient.getApplication("schedule-client-server");
            List<InstanceInfo> instanceInfos= null;
            try {
                  instanceInfos = application.getInstances();
            } catch (Exception e) {
                  log.error("调度未发现可用服务");
            }
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
                             server.setUse(Integer.parseInt(String.valueOf(count)));
                             servers.add(server);
                       }

                 }
            }
            this.checkExecutorBlockStrategyEnum(servers,jobInfoEntity);
            List<Server> enabledServers=new ArrayList<>();
            for(int i=0;i<servers.size();i++){
                  Server server=servers.get(i);
                  if((ExecutorBlockStrategyEnum.TASK_SERIAL.name()).equals(jobInfoEntity.getExecutorBlockStrategy())){
                        boolean flag=redisUtil.hasKey(QUARTZ_HTHT_TASK_SERIAL+":"+jobInfoEntity.getId());
                        if(!flag){
                              enabledServers.add(server);
                        }
                  }
                  if((ExecutorBlockStrategyEnum.CLUSTER_SERIAL.name()).equals(jobInfoEntity.getExecutorBlockStrategy())){
                        long count=redisUtil.scanSize(QUARTZ_HTHT_CLUSTER_SERIAL);
                        if(count==0){
                              enabledServers.add(server);
                        }
                  }

            }
            return enabledServers;
      }
      public abstract void checkExecutorBlockStrategyEnum(List<Server> servers,JobInfoEntity jobInfoEntity);

      public void remote(JobInfoEntity jobInfoEntity,Server server,ResultT<String> resultT){
            //String logId="";
            try {
                  //logId=this.insertLog(jobInfoEntity,server,"0",null);
                  redisUtil.set(QUARTZ_HTHT_PERFORM+":"+server.getHost()+":"+server.getGrpcPort()+":"+jobInfoEntity.getId(),jobInfoEntity.getId(),86400);
                  if((ExecutorBlockStrategyEnum.TASK_SERIAL.name()).equals(jobInfoEntity.getExecutorBlockStrategy())){
                        redisUtil.set(QUARTZ_HTHT_TASK_SERIAL+":"+jobInfoEntity.getId(),jobInfoEntity.getId(),86400);
                  }
                  if((ExecutorBlockStrategyEnum.CLUSTER_SERIAL.name()).equals(jobInfoEntity.getExecutorBlockStrategy())){
                        redisUtil.set(QUARTZ_HTHT_CLUSTER_SERIAL+":"+jobInfoEntity.getId(),jobInfoEntity.getId(),86400);
                  }
                  jobInfoEntity.setExecutorAddress(server.getHost()+":"+server.getGrpcPort());
                  Class<?> target = ExecutorBiz.class;
                  Object invoker = new Object();
                  InvocationHandler invocationHandler = new GrpcServiceProxy<>(target, invoker,server);
                  ExecutorBiz executorBiz = (ExecutorBiz) Proxy.newProxyInstance(GrpcHthtService.class.getClassLoader(), new Class[]{target}, invocationHandler);
                  executorBiz.execute(jobInfoEntity);
            } catch (Exception e) {
                  resultT.setCode(302);
                  redisUtil.del(QUARTZ_HTHT_PERFORM+":"+server.getHost()+":"+server.getGrpcPort()+":"+jobInfoEntity.getId());
                  redisUtil.del(QUARTZ_HTHT_TASK_SERIAL+":"+jobInfoEntity.getId());
                  redisUtil.del(QUARTZ_HTHT_CLUSTER_SERIAL+":"+jobInfoEntity.getId());
                  //this.insertLog(jobInfoEntity,server,"2",logId);
                  e.printStackTrace();
            }
      }

      public abstract  String insertLog(JobInfoEntity jobInfo,Server server,String result,String logId);


}
