package com.piesat.calculate.util

import java.util

import io.krakens.grok.api.{Grok, GrokCompiler, Match}

/**
  * Created by zzj on 2020/3/17.
  */
class GrokUtil {
  def getMesssge(message: String): String = {
    //val pattern: String = "%{CATALINA_JAVA_DATA}  \\[%{JAVA_NAME}\\] - \\[ %{LOGLEVEL} \\]  %{MESSAGE}"
    val pattern: String = "%{CATALINA_JAVA_DATA}  %{LOGLEVEL} %{NUMBER} --- \\[%{JAVA_NAME}\\] %{JAVA_NAME}   :%{MESSAGE}"

    val grokCompiler: GrokCompiler = GrokCompiler.newInstance
    grokCompiler.registerDefaultPatterns()
    grokCompiler.registerPatternFromClasspath("/patterns/javaht")
    val grok: Grok = grokCompiler.compile(pattern)
    val grokMatch: Match = grok.`match`(message)
    val capture: util.Map[String, AnyRef] = grokMatch.capture
    if (capture.isEmpty) {
      return "";
    }
    var result = capture.get("MESSAGE");
    return result.toString;
  }
}
