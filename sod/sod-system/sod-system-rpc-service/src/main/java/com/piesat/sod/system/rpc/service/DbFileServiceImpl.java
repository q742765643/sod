package com.piesat.sod.system.rpc.service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.*;

import com.piesat.common.constant.FileTypesConstant;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.sod.system.dao.DbFileDao;
import com.piesat.sod.system.entity.DbFileEntity;
import com.piesat.sod.system.mapper.DbFileMapper;
import com.piesat.sod.system.rpc.api.DbFileService;
import com.piesat.sod.system.rpc.dto.DbFileDto;
import com.piesat.sod.system.rpc.mapstruct.DbFileMapstruct;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import javax.servlet.http.HttpServletResponse;


/** 数据库文件 业务处理
*@description
*@author wlg
*@date 2019年11月20日下午1:56:46
*
*/
@Service("dbFileService")
public class DbFileServiceImpl extends BaseService<DbFileEntity> implements DbFileService {

	@Autowired
	private DbFileDao dbFileDao;

	@Autowired
	private DbFileMapstruct dbFileMapstruct;

	@Autowired
	private DbFileMapper dbFileMapper;

	@Value("${serverfile.dbfile}")
	private String fileArr;

	@Value("${serverfile.filePath}")
	private String filePath;

	//新增字段模板
	@Value("${serverfile.static.template.add-column}")
	private String addColumn;

	//云数据库申请模板
	@Value("${serverfile.static.template.clouddatabase-application}")
	private String cloudDatabaseApplication;

	//数据库账户申请模板
	@Value("${serverfile.static.template.databaseuser-application}")
	private String databaseUserApplication;

	//数据注册模板
	@Value("${serverfile.static.template.dataregister-application}")
	private String dataRegisterApplication;

	//专题库申请模板
	@Value("${serverfile.static.template.topic-application}")
	private String topicApplication;

	//虚谷客户端
	@Value("${serverfile.static.database-client.xugu-client}")
	private String xuguClient;

	//南大客户端
	@Value("${serverfile.static.database-client.gbase-client}")
	private String gbaseClient;

	//cassandra客户端
	@Value("${serverfile.static.database-client.cassandra-client}")
	private String cassandraClient;



	/**
	 *  获取分页数据
	 * @description
	 * @author wlg
	 * @date 2019-11-20 13:58
	 * @param params
	 * @param pageForm
	 * @return
	 */
	@Override
	public PageBean findPageData(PageForm<DbFileDto> pageForm) throws Exception{
		DbFileEntity dfe = dbFileMapstruct.toEntity(pageForm.getT());
		PageHelper.startPage(pageForm.getCurrentPage(), pageForm.getPageSize());
		if(!StringUtil.isEmpty(dfe.getFileName())) dfe.setFileName("%"+dfe.getFileName()+"%");
		if(!StringUtil.isEmpty(dfe.getFileSuffix())) dfe.setFileSuffix("%"+dfe.getFileSuffix()+"%");

		List<DbFileEntity> data = dbFileMapper.selectList(dfe);
		PageInfo<DbFileEntity> pageInfo = new PageInfo<>(data);

		List<DbFileDto> dtoData = dbFileMapstruct.toDto(pageInfo.getList());
		PageBean page = new PageBean(pageInfo.getTotal(),pageInfo.getPages(),dtoData);

		/*
		 * StringBuffer sb = new StringBuffer();
		 *
		 * sb.append( " SELECT " +
		 * " record_id,file_type,file_name,file_stor_name,file_stor_path, " +
		 * " update_time,file_picture,file_suffix " + " FROM dmin_db_file " +
		 * " where 1=1 "); if(null != dfe.getFileType() &&
		 * !StringUtils.isEmpty(dfe.getFileType())) { sb.append(
		 * " and file_type = '"+dfe.getFileType()+"' "); } if(null != dfe.getFileName()
		 * && !StringUtils.isEmpty(dfe.getFileName())) {
		 * sb.append(" and file_name like '%"+dfe.getFileName()+"%' "); } if(null !=
		 * dfe.getFileSuffix() && !StringUtils.isEmpty(dfe.getFileSuffix())) {
		 * sb.append( " and file_suffix like '%"+dfe.getFileSuffix()+"%' "); } if(null
		 * != dfe.getStartDate() && !StringUtils.isEmpty(dfe.getStartDate())) {
		 * sb.append( " and  update_time >= '"+dfe.getStartDate()+" 00:00:00' "); }
		 * if(null != dfe.getEndDate() && !StringUtils.isEmpty(dfe.getEndDate())) {
		 * sb.append(" and  update_time <= '"+dfe.getEndDate()+" 59:59:59' "); }
		 * sb.append( "order by update_time desc");
		 */
//		PageBean page = this.queryByNativeSQLPageList(sb.toString(), DbFileEntity.class, null, pageForm);
		return page;
	}

	/**
	 *  获取dao
	 * @description
	 * @author wlg
	 * @date 2019-11-20 13:58
	 * @return
	 */
	@Override
	public BaseDao<DbFileEntity> getBaseDao() {
		return dbFileDao;
	}

	/**
	 * 保存上传文件
	 * @description
	 * @author wlg
	 * @date 2019-11-21 10:05
	 * @param request
	 * @throws Exception
	 */
	@Override
	public ResultT save(MultipartHttpServletRequest request){
		try{
			// 获取文件存储路径 , 如果没有 , 创建
			if(!Paths.get(fileArr).toFile().exists()) {
				Paths.get(fileArr).toFile().mkdirs();
			}

			List<MultipartFile> fileList = request.getFiles("file");
			Date now = new Date();
			for(MultipartFile mf:fileList) {
				DbFileEntity dfe = new DbFileEntity();
				String originalFilename = mf.getOriginalFilename();
				//文件路径
				String filePath = fileArr + "/" +originalFilename;
				String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf('.')+1);
				boolean b = Arrays.stream(FileTypesConstant.ALLOW_TYPES).anyMatch(e -> e.equalsIgnoreCase(fileSuffix));
				if (!b){
					return ResultT.failed("文件格式错误");
				}
				String fileType = request.getParameter("fileType");
				dfe.setFileName(originalFilename);
				dfe.setFileSuffix(fileSuffix);
				dfe.setUpdateTime(now);
				dfe.setFileType(fileType);
				dfe.setFileStorName(originalFilename);
				dfe.setFileStorPath(filePath);
				dfe.setFilePictrue("文档");
				//上传文件到服务器指定路径
				FileCopyUtils.copy(mf.getBytes(), new File(filePath));
				dbFileDao.save(dfe);
			}
		}catch (Exception e){
			e.printStackTrace();
			return ResultT.failed("上传失败");
		}
		return ResultT.success();
	}

	/**
	 *  根据id删除
	 * @description
	 * @author wlg
	 * @date 2019-12-23 08:20
	 * @param recordIds
	 * @throws Exception
	 */
	@Override
	public void deleteByIds(String recordIds) throws Exception {
		String[] ids = recordIds.split(",");
		for(String id:ids) {
			DbFileEntity df = dbFileDao.findById(id).orElse(null);
			if(null != df) {
				String filePath = df.getFileStorPath();
				File file = new File(filePath);
				if(file.exists()) {
					file.delete();
				}
				dbFileDao.delete(df);
			}
		}

	}

	@Override
	public void downloadFile(HttpServletResponse response, String name){
		File file = null;
		if ("add-column".equals(name)){
			file = new File(addColumn);
		}else if ("clouddatabase-application".equals(name)){
			file = new File(cloudDatabaseApplication);
		}else if ("databaseuser-application".equals(name)){
			file = new File(databaseUserApplication);
		}else if ("dataregister-application".equals(name)){
			file = new File(dataRegisterApplication);
		}else if ("topic-application".equals(name)){
			file = new File(topicApplication);
		}else if ("xugu-client".equals(name)){
			file = new File(xuguClient);
		}else if ("gbase-client".equals(name)){
			file = new File(gbaseClient);
		}else if ("cassandra-client".equals(name)){
			file = new File(cassandraClient);
		}
		System.out.println(file.getPath());
		InputStream in = null;
		if(file.exists()){
			try {
				String fileName = file.getName();
				response.setCharacterEncoding("UTF-8");
				response.addHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
				response.addHeader("Content-Length", "" + file.length());
				response.setContentType("application/octet-stream");
				OutputStream out = response.getOutputStream();
				in = new FileInputStream(file);
				byte buffer[] = new byte[1024];
				int length = 0;
				while ((length = in.read(buffer)) >= 0){
					out.write(buffer,0,length);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(in != null){
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public DbFileDto fileSave(MultipartFile multipartFile, MultipartHttpServletRequest request) {
		DbFileDto dfe = new DbFileDto();


		Map<String,String> map = new HashMap<>();
		if (multipartFile != null) {
			String originalFileName = multipartFile.getOriginalFilename();//旧的文件名(用户上传的文件名称)
			map.put("fileName",originalFileName);
			dfe.setFileName(originalFileName);
			//新的文件名
			String newFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
			File newFile = new File(filePath + File.separator + newFileName);
			// 判断目标文件所在目录是否存在
			if (!newFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建父目录
				newFile.getParentFile().mkdirs();
			}
			//存入
			try {
				multipartFile.transferTo(newFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}



		return null;
	}

	DbFileDto formFileInfo(String fileName){
		String uuid = com.piesat.common.utils.UUID.randomUUID().toString().replaceAll("-", "");
		DbFileDto df = new DbFileDto();
		df.setFileName(fileName);
		String sourcePath = filePath + File.separator + uuid + "_" + fileName;
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		if (".pdf".equals(suffix.toLowerCase())){
		}
		String name = fileName.substring(0, fileName.lastIndexOf("."));
		String targetPath = filePath + File.separator + uuid + "_" + name + ".pdf";
		Map<String, String> map = new HashMap<>();
		map.put("sourcePath",sourcePath);
		map.put("targetPath",targetPath);
		return df;
	}


}
