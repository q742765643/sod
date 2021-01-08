package com.piesat.util.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

public class CloudHttpUtil {

    public static String  getToken(String loginUrl,String cloudName,String cloudPassword) {

//        String url = "http://10.20.64.167:8081/api/cloud/iams/login";
        String url = loginUrl;
        JSONObject jsonStr = new JSONObject();
        jsonStr.put("username",cloudName);
        jsonStr.put("password", cloudPassword);
        String jsonStr1 = JSON.toJSONString(jsonStr);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();
            // 创建http GET请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(jsonStr1));
            // 执行请求
            response = httpclient.execute(httpPost);
//            System.out.println(response);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Object jsonObject;

        JSONObject jo = JSON.parseObject(resultString);
        String a = jo.getString("content");
        JSONObject ja = JSON.parseObject(a);
        String b = ja.getString("tokenInfo");
        JSONObject jb = JSON.parseObject(b);
        String token = jb.getString("access_token");
//        System.out.println(token);

        return token;
//        return resultT.success(resultString);
    }


    public static String doGet(String url,String loginUrl,String cloudName,String cloudPassword) throws UnsupportedEncodingException {

//        BasicConfigurator.configure();
        CloseableHttpClient httpClient = HttpClients.createDefault();
//        String urlq = URLEncoder.encode(url);
        System.out.println(url);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept","application/json,text/plain,*/*");
        httpGet.setHeader("Content-type", "application/json;charset=UTF-8");
        httpGet.setHeader("DataEncoding", "UTF-8");
        httpGet.setHeader("Authorization", "Bearer "+getToken(loginUrl,cloudName,cloudPassword));
//        httpGet.setHeader("token", token);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
//            logger.info("请求成功");
//            System.out.println(result);
            return result;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 原生字符串发送post请求
     *
     * @param url
     * @param jsonStr
     * @return
     * @throws IOException
     */
    public static String doPost(String url, String jsonStr,String loginUrl,String cloudName,String cloudPassword) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Accept","application/json, text/plain,*/*");
        httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
        httpPost.setHeader("DataEncoding", "UTF-8");
        httpPost.setHeader("Authorization", "Bearer "+getToken(loginUrl,cloudName,cloudPassword));

        CloseableHttpResponse httpResponse = null;
        try {
            httpPost.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
//            System.out.println(result);
            return result;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 原生字符串发送put请求
     *
     * @param url
     * @param jsonStr
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doPut(String url, String jsonStr,String loginUrl,String cloudName,String cloudPassword) throws UnsupportedEncodingException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpPut.setConfig(requestConfig);
        httpPut.setHeader("Accept","application/json,text/plain,*/*");
        httpPut.setHeader("Accept-Encoding","gzip, deflate");
        httpPut.setHeader("Cache-Control","no-cache");
        httpPut.setHeader("Connection", "keep-alive");
        httpPut.setHeader("Content-type", "application/json;charset=UTF-8");
        httpPut.setHeader("DataEncoding", "UTF-8");
        httpPut.setHeader("Authorization", "Bearer "+getToken(loginUrl,cloudName,cloudPassword));
//        String finalString = null;
        CloseableHttpResponse httpResponse = null;
//        finalString= EntityUtils.toString(jsonStr, "UTF-8");
        jsonStr = new String (jsonStr.getBytes(),"ISO-8859-1");
//        System.out.println("请求情况+++++"+jsonStr);
        try {
            httpPut.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpPut);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
//            System.out.println("请求情况"+result);
            return result;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 发送delete请求
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doDelete(String url,String loginUrl,String cloudName,String cloudPassword) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpDelete.setConfig(requestConfig);
        httpDelete.setHeader("Content-type", "application/json");
        httpDelete.setHeader("DataEncoding", "UTF-8");
        httpDelete.setHeader("Authorization", "Bearer "+getToken(loginUrl,cloudName,cloudPassword));
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpDelete);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
