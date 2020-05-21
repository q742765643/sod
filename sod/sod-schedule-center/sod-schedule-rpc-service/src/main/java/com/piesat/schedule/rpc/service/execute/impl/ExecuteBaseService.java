package com.piesat.schedule.rpc.service.execute.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.client.api.client.handler.base.BaseHandler;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.enums.ExecutorBlockStrategyEnum;
import com.piesat.schedule.rpc.proxy.GrpcServiceProxy;
import com.piesat.schedule.rpc.thread.ScheduleThread;
import com.piesat.schedule.rpc.vo.Server;
import com.piesat.sso.client.util.RedisUtil;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 17:47
 */
@Slf4j
@Service("executeBaseService")
public abstract class ExecuteBaseService {
      private  ConcurrentHashMap<String, ExecutorBiz> biz=new ConcurrentHashMap<>();
      public static final ExecutorService sendLocalPool = new ThreadPoolExecutor(30, 30,
              0L, TimeUnit.MILLISECONDS,
              new LinkedBlockingQueue<Runnable>(20000), new ThreadFactoryBuilder().setNameFormat("do-send-local-pool-%d").build(), new ThreadPoolExecutor.AbortPolicy());
      protected static final String QUARTZ_HTHT_PERFORM="QUARTZ:HTHT:PERFORM";
      protected static final String QUARTZ_HTHT_TASK_SERIAL="QUARTZ:HTHT:SINGLE:SERIAL";
      protected static final String QUARTZ_HTHT_CLUSTER_SERIAL="QUARTZ:HTHT:CLUSTER:SERIAL";

      @Autowired
      private RedisUtil redisUtil;

      public  void executeBusiness(JobInfoEntity jobInfoEntity, ResultT<String> resultT){

            String handler=jobInfoEntity.getType().toLowerCase()+"Handler";
            if(null==jobInfoEntity.getExecutorHandler()|| !StringUtils.isNotNullString(jobInfoEntity.getExecutorHandler())){
                  jobInfoEntity.setExecutorHandler(handler);
            }

            BaseHandler baseHandler= (BaseHandler) SpringUtil.getBeanOrNull(jobInfoEntity.getExecutorHandler());

            if(null==baseHandler){
                  List<Server> servers=this.findServer(jobInfoEntity,resultT);
                  if(!resultT.isSuccess()){
                        return;
                  }
                  Server server=null;
                  if(servers.size()==1){
                        server=servers.get(0);
                  }else {
                        server=this.operationalControl(jobInfoEntity,servers,resultT);
                  }
                  if(null==server){
                        resultT.setCode(301);
                        return;
                  }
                  log.info("{}当前任务条数{}",server.getHost(),server.getUse());
                  this.remote(jobInfoEntity,server,resultT);
            }else{
                  sendLocalPool.execute(
                          ()->{
                                baseHandler.execute(jobInfoEntity,resultT);
                          }
                  );
            }



      }
      public  Server operationalControl(JobInfoEntity jobInfoEntity,List<Server> servers,ResultT<String> resultT){
            if(servers.size()==0){
                  return null;
            }
            Collections.sort(servers, new Comparator<Server>() {

                  @Override
                  public int compare(Server o1, Server o2) {
                     float a=o1.getUse()%o1.getLimit();
                     float b=o2.getUse()%o2.getLimit();
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


      public List<Server> findServer(JobInfoEntity jobInfoEntity,ResultT<String> resultT){
            List<Server> servers=new ArrayList<>();
            DiscoveryClient discoveryClient= SpringUtil.getBean(DiscoveryClient.class);
            Application application=discoveryClient.getApplication(GrpcConstant.SCHEDULE_CLIENT_SERVER);
            List<InstanceInfo> instanceInfos= null;
            try {
                  instanceInfos = application.getInstances();
            } catch (Exception e) {
                  resultT.setCode(309);
                  log.error("调度未发现可用服务");
            }
            if(instanceInfos==null||instanceInfos.size()==0){
                  try {
                        Thread.sleep(60000);
                  } catch (InterruptedException e) {
                        e.printStackTrace();
                  }
                  resultT.setCode(309);
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
                  String serviceName=server.getHost()+":"+server.getGrpcPort();
                  ExecutorBiz executorBiz=biz.get(serviceName);
                  if(executorBiz==null){
                        Class<?> target = ExecutorBiz.class;
                        Object invoker = new Object();
                        InvocationHandler invocationHandler = new GrpcServiceProxy<>(target, invoker,server);
                        executorBiz = (ExecutorBiz) Proxy.newProxyInstance(GrpcHthtService.class.getClassLoader(), new Class[]{target}, invocationHandler);
                        biz.put(serviceName,executorBiz);
                  }

                  executorBiz.execute(jobInfoEntity);

            } catch (Exception e) {
                  log.error("调度出错:{}", OwnException.get(e));
                  resultT.setCode(302);
                  redisUtil.del(QUARTZ_HTHT_PERFORM+":"+server.getHost()+":"+server.getGrpcPort()+":"+jobInfoEntity.getId());
                  redisUtil.del(QUARTZ_HTHT_TASK_SERIAL+":"+jobInfoEntity.getId());
                  redisUtil.del(QUARTZ_HTHT_CLUSTER_SERIAL+":"+jobInfoEntity.getId());
                  //this.insertLog(jobInfoEntity,server,"2",logId);
            }
      }


      public static void main(String[] args){
            List<Server> servers=new ArrayList<>();
            Server server1=new Server();
            server1.setUse(60);
            Server server2=new Server();
            server2.setUse(40);
            servers.add(server1);
            servers.add(server2);
            Collections.sort(servers, new Comparator<Server>() {

                  @Override
                  public int compare(Server o1, Server o2) {
                        float a=o1.getUse()%o1.getLimit();
                        System.out.println(o1.getUse());
                        System.out.println(a);

                        float b=o2.getUse()%o2.getLimit();
                        System.out.println(b);

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
            System.out.println(JSON.toJSONString(servers.get(0)));
      }


}
