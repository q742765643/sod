package com;

import io.krakens.grok.api.GrokCompiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-13 13:29
 **/
public class Log4jTestDemo {
    private static Logger logger = LoggerFactory.getLogger(Log4jTestDemo.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        //String pattern="%{CATALINA_JAVA_DATA}  \\[%{JAVA_NAME}\\] - \\[ %{LOGLEVEL} \\]  %{MESSAGE}";
       //        String pattern="\\[%{CATALINA_JAVA_DATA}\\] \\[%{JAVA_NAME}\\] \\[%{LOGLEVEL} \\] %{JAVACLASS} - %{MESSAGE}";
        String pattern="%{CATALINA_JAVA_DATA} %{LOGLEVEL} %{NUMBER} --- \\[%{JAVA_NAME}\\] %{JAVA_NAME}   :%{MESSAGE}";
         pattern="\\[%{CATALINA_JAVA_DATA}\\] \\[%{JAVA_NAME}\\] \\[%{LOGLEVEL}%{JAVA_NAME}\\] %{JAVA_NAME}:%{MESSAGE}";

        //String pattern="%{YEAR}-%{MONTHNUM}-%{MONTHDAY} %{HOUR}:?%{MINUTE}(?::?%{SECOND}) %{JAVACLASS} %{MYSELF} %{LOGLEVEL} %{MYSELF}";
        String message = "2020-03-13 16:18:01  [ main:1321137 ] - [ DEBUG ]  {'name':'卢本伟\n" +
                "','age':24,'Hero':{'name':'Fizz','Position':'Mid','charactor':'killer'},'nickNames':['五五开','芦苇','white'],'Honors':[{'year':2011,'name':'TGA总决赛冠军'},{'year':2013,'name':'S3全球总决赛中国区冠军'},{'year':2013,'name':'S3全球总决赛亚军'}]}";
        message="2020-04-08 09:35:28.127  INFO 209960 --- [           main] c.p.s.client.ScheduleClientApplication   : Started ScheduleClientApplication in 15.317 seconds (JVM running for 16.101)";
        message="[2020-05-11 17:34:52.453] [main] [INFO ] org.hibernate.Version - HHH000412  : Hibernate Core {5.3.7.Final}";
        //String message="[2020-03-12 09:36:13.752] [main1-11] [INFO ] o.s.d.r.config.RepositoryConfigurationDelegate - Finished Spring Data repository scanning in 504ms. Found 35 repository interfaces.";
        /* Match match = null;
        try {
            Grok grok = new Grok();
            //添加patter配置文件,默认的grok的pattern是null
            grok.addPatternFromFile(GROK_PATTERN_PATH);
            //添加自定义pattern，当然%{IPV4}可以不用已有pattern，也可以自定义正则表达式
            grok.compile(pattern);
            match = grok.match(message);
            match.captures();
            if(!match.isNull()){
                System.out.println(match.toMap().toString());
                System.out.println(match.toJson().toString());
            }else{
                System.out.println("not match");
            }
        } catch (GrokException e) {
            e.printStackTrace();
            match = null;
        }*/
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        grokCompiler.registerDefaultPatterns();
        grokCompiler.registerPatternFromClasspath("/patterns/java");
        final io.krakens.grok.api.Grok grok = grokCompiler.compile(pattern);
        io.krakens.grok.api.Match grokMatch = grok.match(message);
        final Map<String, Object> capture = grokMatch.capture();
        System.out.println(capture);
  /*      while (true){
            // System.out.println("This is println message.");
            String jsonString = "{'name':'卢本伟\r\n','age':24,'Hero':{'name':'Fizz','Position':'Mid','charactor':'killer'},'nickNames':['五五开','芦苇','white'],'Honors':[{'year':2011,'name':'TGA总决赛冠军'},{'year':2013,'name':'S3全球总决赛中国区冠军'},{'year':2013,'name':'S3全球总决赛亚军'}]}";
            // 记录debug级别的信息
            logger.info(jsonString);
            // 记录info级别的信息
            //logger.info("This is info message.");
            // 记录error级别的信息
            //logger.error("This is error message.");
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }
}

