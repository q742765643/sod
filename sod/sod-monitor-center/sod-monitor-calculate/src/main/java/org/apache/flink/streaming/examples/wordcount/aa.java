package org.apache.flink.streaming.examples.wordcount;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-29 20:07
 **/
public class aa {
    public void aa(){
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
        RestHighLevelClient restHighLevelClient=null;

        BulkProcessor bulkProcessor = BulkProcessor.builder(
                (request, bulkListener) ->
                        restHighLevelClient.bulkAsync(request, bulkListener),
                listener).build();
    }
}

