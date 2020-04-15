package com.piesat.calculate.util;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-29 20:22
 **/
public class BulkProcessorUtil {
    private static BulkProcessor bulkProcessor=null;

    public static BulkProcessor bulk(){
        if(bulkProcessor!=null){
            return bulkProcessor;
        }
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {

            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {

            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  Throwable failure) {

            }
        };
       /* bulkProcessor = BulkProcessor.builder(
                EsUtil.restHighLevelClient()::bulkAsync,
                listener).setBulkActions(1)
                .setBulkSize(new ByteSizeValue(5L, ByteSizeUnit.MB))
                .setConcurrentRequests(0)
                //.setFlushInterval(TimeValue.timeValueSeconds(5L))
                // 重试3次，间隔1s
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3)).build();*/
        return bulkProcessor;

    }
}

