package com.piesat.dm.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

public class FreeMarkerUtil {

	private Configuration configuration = null;

	public FreeMarkerUtil() {
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
	}

	public File createDoc(Map<String, Object> dataMap, String fileName, String modelPath,HttpServletRequest request) throws Exception {
		Writer out = null;
		FileOutputStream fos = null;
		try {
			/*String path = ResourceUtils.getURL("classpath:").getPath();
			System.out.println("path"+path);
			File file = new File(path);
			File filePath = new File(file,modelname);*/
			File filePath = new File(modelPath);
			String modelname = null;
			//设置模板文件路径
			if(filePath.exists()){
				modelname = filePath.getName();
				File basepath = new File(modelPath.replace(modelname,""));
				configuration.setDirectoryForTemplateLoading(basepath);
			}else{
				//模板文件不存在，将使用项目中备用的模板
				configuration.setClassForTemplateLoading(FreeMarkerUtil.class, "/com/piesat");
			}
			//取出模板文件
			Template template = configuration.getTemplate(modelname);
			// 输出文档路径及名称
			File outFile = new File(fileName);
			fos = new FileOutputStream(outFile);
			OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
			// 这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，
			//但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。
			// out = new BufferedWriter(new OutputStreamWriter(new
			// FileOutputStream(outFile)));
			out = new BufferedWriter(oWriter);
			template.process(dataMap, out);
			return outFile;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally{
			//代码审查修改的
			if(out != null){
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos != null){
				try {
					fos.close();
					fos = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
//			try {
//				out.close();
//				fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}
}
