package com.piesat.schedule.rpc.service.nas.quota;

import com.alibaba.fastjson.JSON;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * @ClassName : BaseQuota
 * @Description :
 * @Author : zzj
 * @Date: 2020-09-14 17:54
 */
@Slf4j
public abstract class BaseQuota {
  public abstract ResultT<String> add(String path, BigDecimal hardThreshold);

  public abstract ResultT<String> update(String path, BigDecimal hardThreshold);

  public abstract ResultT<String> del(String path);

  public ResultT<String> send(String url,String messge) {
      ResultT<String> resultT = new ResultT<>();
      try {
          SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
          requestFactory.setConnectTimeout(2 * 1000);
          requestFactory.setReadTimeout(2 * 1000);
          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
          headers.add("Accept", MediaType.APPLICATION_JSON.toString());
          HttpEntity<String> httpEntity = new HttpEntity<>(messge, headers);
          RestTemplate rst = new RestTemplate(requestFactory);
          ResponseEntity<String> stringResponseEntity = rst.postForEntity(url, httpEntity, String.class);
          resultT.setData(stringResponseEntity.getBody());
          log.info("nas申请配额:" + JSON.toJSONString(stringResponseEntity));
      } catch (Exception e) {
          log.error("nas申请配额异常:" + messge);
          resultT.setErrorMessage("nas申请配额异常");
      }
      return resultT;
  }

}

