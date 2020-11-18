package com.piesat.portal.rpc.service;

import com.github.pagehelper.util.StringUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.StringUtils;
import com.piesat.portal.dao.ApiCodeDao;
import com.piesat.portal.dao.ApiManageDao;
import com.piesat.portal.dao.ApiParamDao;
import com.piesat.portal.dao.ApiResDao;
import com.piesat.portal.entity.ApiCodeEntity;
import com.piesat.portal.entity.ApiManageEntity;
import com.piesat.portal.entity.ApiParamEntity;
import com.piesat.portal.entity.ApiResEntity;
import com.piesat.portal.rpc.api.ApiManageService;
import com.piesat.portal.rpc.dto.ApiCodeDto;
import com.piesat.portal.rpc.dto.ApiManageDto;
import com.piesat.portal.rpc.dto.ApiParamDto;
import com.piesat.portal.rpc.dto.ApiResDto;
import com.piesat.portal.rpc.mapstruct.ApiCodeMapstruct;
import com.piesat.portal.rpc.mapstruct.ApiManageMapstruct;
import com.piesat.portal.rpc.mapstruct.ApiParamMapstruct;
import com.piesat.portal.rpc.mapstruct.ApiResMapstruct;
import com.piesat.ucenter.rpc.dto.system.UserDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("apiManageService")
public class ApiManageServiceImpl extends BaseService<ApiManageEntity> implements ApiManageService {

    @Autowired
    private ApiManageDao apiManageDao;

    @Autowired
    private ApiCodeDao apiCodeDao;

    @Autowired
    private ApiParamDao apiParamDao;

    @Autowired
    private ApiResDao apiResDao;

    @Autowired
    private ApiManageMapstruct apiManageMapstruct;

    @Autowired
    private ApiCodeMapstruct apiCodeMapstruct;

    @Autowired
    private ApiParamMapstruct apiParamMapstruct;

    @Autowired
    private ApiResMapstruct apiResMapstruct;

    @Override
    public BaseDao<ApiManageEntity> getBaseDao() {
        return apiManageDao;
    }

    @Override
    public PageBean selectPageList(PageForm<ApiManageDto> pageForm) {
        ApiManageDto apiManageDto = pageForm.getT();
        SimpleSpecificationBuilder specificationBuilder=new SimpleSpecificationBuilder();
        if(StringUtils.isNotEmpty(apiManageDto.getApiName())){
            specificationBuilder.add("apiName", SpecificationOperator.Operator.likeAll.name(),apiManageDto.getApiName());
        }
        if(StringUtils.isNotEmpty(apiManageDto.getApiDesc())){
            specificationBuilder.add("apiDesc", SpecificationOperator.Operator.likeAll.name(),apiManageDto.getApiDesc());
        }
        if(StringUtils.isNotEmpty(apiManageDto.getApiSys())){
            specificationBuilder.add("apiSys", SpecificationOperator.Operator.eq.name(),apiManageDto.getApiSys());
        }
        PageBean pageBean=this.getPage(specificationBuilder.generateSpecification(),pageForm,null);
        List<ApiManageEntity> apiManageEntities = (List<ApiManageEntity>) pageBean.getPageData();
        List<ApiManageDto> apiManageDtos = apiManageMapstruct.toDto(apiManageEntities);
        pageBean.setPageData(apiManageDtos);
        return pageBean;
    }

    @Override
    public Map<String, Object> getDotById(String id) {

        Map<String,Object> result = new HashMap<String,Object>();
        ApiManageEntity apiManageEntity = this.getById(id);
        List<ApiCodeEntity> apiCodeEntities = apiCodeDao.findByApiId(id);
        List<ApiParamEntity> apiParamEntities = apiParamDao.findByApiId(id);
        List<ApiResEntity> apiResEntities = apiResDao.findByApiId(id);

        result.put("api", apiManageEntity);
        result.put("param", apiParamEntities);
        result.put("res", apiResEntities);
        result.put("code", apiCodeEntities);
        return result;
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteApi(String id) {
        deleteApiInfo(id);
        //删除基本信息
        apiManageDao.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void deleteApiInfo(String apiId){
        //删除样例代码
        apiCodeDao.deleteByApiId(apiId);
        //删除返回值信息
        apiResDao.deleteByApiId(apiId);
        //删除参数信息
        apiParamDao.deleteByApiId(apiId);
    }

    @Transactional(readOnly = false)
    @Override
    public ApiManageDto saveDto(ApiManageDto apiManageDto) {
        apiManageDto.setStatus("0");
        ApiManageEntity apiManageEntity = apiManageMapstruct.toEntity(apiManageDto);
        apiManageEntity = apiManageDao.saveNotNull(apiManageEntity);

        List<ApiCodeDto> apiCodeDtos = apiManageDto.getApiCodeDtos();
        List<ApiCodeEntity> apiCodeEntities = apiCodeMapstruct.toEntity(apiCodeDtos);
        if(apiCodeEntities != null && apiCodeEntities.size() > 0){
            for(int i=0;i<apiCodeEntities.size();i++){
                apiCodeEntities.get(i).setApiId(apiManageEntity.getId());
                apiCodeDao.saveNotNull(apiCodeEntities.get(i));
            }
        }

        List<ApiParamDto> apiParamDtos = apiManageDto.getApiParamDtos();
        List<ApiParamEntity> apiParamEntities = apiParamMapstruct.toEntity(apiParamDtos);
        if(apiParamEntities != null && apiParamEntities.size()>0){
            for(int i=0;i<apiParamEntities.size();i++){
                apiParamEntities.get(i).setApiId(apiManageEntity.getId());
                apiParamDao.saveNotNull(apiParamEntities.get(i));
            }
        }

        List<ApiResDto> apiResDtos = apiManageDto.getApiResDtos();
        List<ApiResEntity> apiResEntities = apiResMapstruct.toEntity(apiResDtos);
        if(apiResEntities != null && apiResEntities.size()>0){
            for(int i=0;i<apiResEntities.size();i++){
                apiResEntities.get(i).setApiId(apiManageEntity.getId());
                apiResDao.saveNotNull(apiResEntities.get(i));
            }
        }

        return apiManageMapstruct.toDto(apiManageEntity);
    }

    @Override
    public ApiManageDto updateDto(ApiManageDto apiManageDto) {
        deleteApiInfo(apiManageDto.getId());
        apiManageDto = saveDto(apiManageDto);
        return apiManageDto;
    }

    @Override
    public ResultT uploadFile(MultipartFile file,String apiSys) {
        //获取excel
        Workbook wb = getWorkBoot(file);
        //获取sheet
        Sheet sheet = wb.getSheetAt(0);
        if(null == sheet){
            return ResultT.failed("导入文件错误，请使用导入样例模板");
        }
        int firstRow = 2;
        //获取数据结尾行
        int lastRow = sheet.getLastRowNum();

        final int apiName = 0,//接口名称列
                apiDesc = 1,//接口描述列
                apiHttpType=2,//请求方式列
                apiExa=3,//调用示例列
                apiOrders = 4,//接口排序列
                apiRemark = 5,//接口备注列

                paramName=6,//参数名称列
                paramDesc=7,//参数中文描述列
                paramType=8,//参数类型列
                paramNeed=9,//是否必填列
                paramOrder=10,//参数排序列

                respName = 11,//返回值名称列
                respDesc=12,//返回值描述列
                respType = 13,//返回值类型列
                respOrder = 14,//返回值排序列

                codeLang = 15,//编程语言列
                codeType = 16,//调用方式列
                respClass = 17,//返回值格式列
                paramExa = 18,//参数示例
                useExa=19,//调用示例
                respExa=20,//返回值示例
                codeOrder=21;//样例代码排序

        for(int i = firstRow;i<=lastRow;i++) {
            //获取一条记录
            Row row = sheet.getRow(i);
            //第一列是接口名称 ,直到下一个接口名称有值 , 之间的所有行都是当前接口信息
            String apiNameStr = getCellValue(row.getCell(apiName));

            ApiManageEntity apiManageEntity = new ApiManageEntity();
            if(StringUtil.isNotEmpty(apiNameStr)) {
                //接口基本信息
                apiManageEntity.setApiSys(apiSys);
                apiManageEntity.setApiName(getCellValue(row.getCell(apiName)));
                apiManageEntity.setApiDesc(getCellValue(row.getCell(apiDesc)));
                String httpType = getCellValue(row.getCell(apiHttpType));
                if(StringUtil.isNotEmpty(httpType) && "GET".equals(httpType.toUpperCase())) {
                    apiManageEntity.setApiHttptype("G");
                }else if(!StringUtil.isEmpty(httpType) &&"POST".equals(httpType.toUpperCase())) {
                    apiManageEntity.setApiHttptype("P");
                }else {
                    apiManageEntity.setApiHttptype("A");
                }
                apiManageEntity.setApiExample(getCellValue(row.getCell(apiExa)));
                apiManageEntity.setSerialNumber(getCellValue(row.getCell(apiOrders)));
                apiManageEntity.setRemark(getCellValue(row.getCell(apiRemark)));
                apiManageEntity = apiManageDao.saveNotNull(apiManageEntity);
            }

            if(StringUtil.isEmpty(apiManageEntity.getId())){
                continue;
            }

            //之后每一行都有接口 参数信息 , 返回值信息 , 样例代码信息 , 根据每个实体的第一个是否有值来判断是否有对应信息

            //参数信息
            String paramNameStr = getCellValue(row.getCell(paramName));
            if(StringUtil.isNotEmpty(paramNameStr)) {
                ApiParamEntity ape = new ApiParamEntity();
                ape.setApiId(apiManageEntity.getId());
                ape.setParamName(paramNameStr);
                ape.setParamDesc(getCellValue(row.getCell(paramDesc)));
                ape.setParamType(getCellValue(row.getCell(paramType)));
                ape.setIsneed(getCellValue(row.getCell(paramNeed)));
                ape.setSerialNumber(getCellValue(row.getCell(paramOrder)));
                apiParamDao.saveNotNull(ape);
            }
            //返回值信息
            String respNameStr = getCellValue(row.getCell(respName));
            if(StringUtil.isNotEmpty(respNameStr)) {
                ApiResEntity are = new ApiResEntity();
                are.setApiId(apiManageEntity.getId());
                are.setResName(respNameStr);
                are.setResDesc(getCellValue(row.getCell(respDesc)));
                are.setResType(getCellValue(row.getCell(respType)));
                are.setSerialNumber(getCellValue(row.getCell(respOrder)));
                apiResDao.saveNotNull(are);
            }

            //样例代码信息
            String codeLangStr = getCellValue(row.getCell(codeLang));
            if(StringUtil.isNotEmpty(codeLangStr)) {
                ApiCodeEntity ace = new ApiCodeEntity();
                ace.setApiId(apiManageEntity.getId());
                ace.setCodeLang(codeLangStr);
                ace.setUseType(getCellValue(row.getCell(codeType)));
                ace.setResType(getCellValue(row.getCell(respClass)));
                ace.setParamCode(getCellValue(row.getCell(paramExa)));
                ace.setUseCode(getCellValue(row.getCell(useExa)));
                ace.setResCode(getCellValue(row.getCell(respExa)));
                ace.setSerialNumber(getCellValue(row.getCell(codeOrder)));
                apiCodeDao.saveNotNull(ace);
            }
        }
        return ResultT.success();
    }

    @Override
    public List<ApiManageDto> findByApiSys(ApiManageDto apiManageDto) {
        List<ApiManageEntity> apiManageEntities = new ArrayList<ApiManageEntity>();
        if(StringUtil.isNotEmpty(apiManageDto.getApiName())){
            apiManageEntities = apiManageDao.findByApiSysAndApiName(apiManageDto.getApiSys(),apiManageDto.getApiName());
        }else{
           apiManageEntities = apiManageDao.findByApiSys(apiManageDto.getApiSys());
        }
        List<ApiManageDto> apiManageDtos = apiManageMapstruct.toDto(apiManageEntities);
        return apiManageDtos;
    }

    @Override
    public List<ApiManageDto> findApiAll() {
        List<ApiManageEntity> apiManageEntities = this.getAll();
        List<ApiManageDto> apiManageDtos = apiManageMapstruct.toDto(apiManageEntities);
        return apiManageDtos;
    }

    /**
     * 获取workbook
     * @param file
     * @return
     */
    private Workbook getWorkBoot(MultipartFile file){
        String fileName = file.getOriginalFilename();
        Workbook wb = null;
        try {
            InputStream is = file.getInputStream();
            if(fileName.endsWith("xls")) {
                wb = new HSSFWorkbook(is);
            }else if(fileName.endsWith("xlsx")) {
                wb = new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;

    }

    /**
     * 获取单元格值
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC: //数字
                cellValue = cell.getNumericCellValue()+"";
                break;
            case STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: //空值
                cellValue = "";
                break;
            case ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


}
