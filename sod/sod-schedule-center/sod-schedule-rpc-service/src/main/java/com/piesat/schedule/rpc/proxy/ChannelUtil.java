package com.piesat.schedule.rpc.proxy;

import com.piesat.schedule.rpc.vo.Server;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.DnsNameResolverProvider;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-17 12:10
 **/
public class ChannelUtil {
    public static ConcurrentHashMap<String, ManagedChannel> grpcChannel=new ConcurrentHashMap<>();

    public  static synchronized ManagedChannel getChannel(String serviceName,Server grpcServer){
        ManagedChannel channel=null;
        if(null!=grpcChannel.get(serviceName)){
            channel=grpcChannel.get(serviceName);
        }else{
            channel = ManagedChannelBuilder.forAddress(grpcServer.getHost(), grpcServer.getGrpcPort())
                    .defaultLoadBalancingPolicy("round_robin")
                    .nameResolverFactory(new DnsNameResolverProvider())
                    .usePlaintext().build();
            grpcChannel.put(serviceName,channel);
        }
        return channel;

    }
}

