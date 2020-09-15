package com.piesat.schedule.rpc.service.nas;

import cmadaas.dpl.grpc.nas.NasAccredit;
import cmadaas.dpl.grpc.nas.NasAccreditServiceGrpc;
import cn.hutool.core.io.FileUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.jpa.specification.SimpleSpecificationBuilder;
import com.piesat.common.jpa.specification.SpecificationOperator;
import com.piesat.common.utils.Doc2PDF;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.nas.NasDetailsDao;
import com.piesat.schedule.dao.nas.NasManageDao;
import com.piesat.schedule.dao.nas.NasQuotaDao;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.nas.NasDetailsEntity;
import com.piesat.schedule.entity.nas.NasManageEntity;
import com.piesat.schedule.entity.nas.NasQuotaEntity;
import com.piesat.schedule.rpc.api.nas.NasManageService;
import com.piesat.schedule.rpc.dto.backup.MetaBackupDto;
import com.piesat.schedule.rpc.dto.nas.NasManageDto;
import com.piesat.schedule.rpc.dto.nas.NasManageFileDto;
import com.piesat.schedule.rpc.mapstruct.nas.NasManageMapstruct;
import com.piesat.schedule.rpc.service.nas.quota.BaseQuota;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.swagger.annotations.ApiModelProperty;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class NasManageServiceImpl extends BaseService<NasManageEntity> implements NasManageService {
    @Autowired
    private NasManageDao nasManageDao;
    @Autowired
    private NasQuotaDao nasQuotaDao;
    @Autowired
    private NasDetailsDao nasDetailsDao;
    @Autowired
    private NasManageMapstruct nasManageMapstruct;
    @Value("${fileUpload.savaPath:/zzj/data/upload}")
    private String savePath;
    @Value("${fileUpload.httpPath:/upload}")
    private String httpPath;

    @Override
    public BaseDao<NasManageEntity> getBaseDao() {
        return nasManageDao;
    }
    /**
    * @description 分页查询nas申请信息
    * @author zzj
    * @date 2020/9/7 11:12
    * @return void
    * @throws
    */
    public PageBean selectPageList(PageForm<NasManageDto> pageForm){
        NasManageEntity nasManageEntity=nasManageMapstruct.toEntity(pageForm.getT());
        SimpleSpecificationBuilder spec=new SimpleSpecificationBuilder();
        if(StringUtils.isNotNullString(nasManageEntity.getUserName())){
            spec.add("userName", SpecificationOperator.Operator.likeAll.name(),nasManageEntity.getUserName());
        }
        if(StringUtils.isNotNullString(nasManageEntity.getAuditStatus())){
            spec.add("auditStatus", SpecificationOperator.Operator.eq.name(),nasManageEntity.getAuditStatus());
        }
        if(StringUtils.isNotNullString(nasManageEntity.getIsDocker())){
            spec.add("isDocker", SpecificationOperator.Operator.eq.name(),nasManageEntity.getIsDocker());
        }
        if(StringUtils.isNotNullString((String) nasManageEntity.getParamt().get("beginTime"))){
            spec.add("auditTime", SpecificationOperator.Operator.ges.name(),(String) nasManageEntity.getParamt().get("beginTime"));
        }
        if(StringUtils.isNotNullString((String) nasManageEntity.getParamt().get("endTime"))){
            spec.add("auditTime",SpecificationOperator.Operator.les.name(),(String) nasManageEntity.getParamt().get("endTime"));
        }
        Sort sort=Sort.by(Sort.Direction.DESC,"createTime");
        PageBean pageBean=this.getPage(spec.generateSpecification(),pageForm,sort);
        List<NasManageEntity> nasManageEntities= (List<NasManageEntity>) pageBean.getPageData();
        pageBean.setPageData(nasManageMapstruct.toDto(nasManageEntities));
        return pageBean;
    }

    /**
    * @description 根据id获取nas审核信息
    * @author zzj
    * @date 2020/9/7 11:47
    * @return com.piesat.schedule.rpc.dto.nas.NasManageDto
    * @throws
    */
    public NasManageDto findById(String id){
        NasManageEntity nasManageEntity=this.getById(id);
        return nasManageMapstruct.toDto(nasManageEntity);
    }

    /**
    * @description 保存和修改nas申请信息 和配额接口调用
    * @author zzj
    * @date 2020/9/7 11:48
    * @return void
    * @throws
    */
    @SneakyThrows
    @Transactional
    public void add(NasManageFileDto nasManageFileDto, ResultT<String> resultT){
        MultipartFile blFile =nasManageFileDto.getBlFile();
        NasManageDto nasManageDto=new NasManageDto();
        BeanUtils.copyProperties(nasManageFileDto,nasManageDto);
        if(!blFile.isEmpty()){
           String realPath=savePath+"/nas";
           String fileName=blFile.getOriginalFilename();
           File file =new File(realPath+"/"+fileName);
           if(file.exists()){
               resultT.setErrorMessage("申请材料重复,请选择新的申请材料或者重命名!");
               return;
           }
           if(!file.getParentFile().exists()){
               file.getParentFile().mkdirs();
           }
            blFile.transferTo(file);
            String prefix=FileUtil.getPrefix(fileName);
            String pdfPath=realPath+"/"+prefix+".pdf";
            try {
                Doc2PDF.doc2pdf(file.getPath(), pdfPath);
            } catch (Exception e) {
                resultT.setErrorMessage("申请材料转pdf失败!");
                FileUtil.del(file);
                FileUtil.del(pdfPath);
                return;
            }
            nasManageDto.setReviewMaterials(file.getPath());
            nasManageDto.setReviewMaterialsPdf(httpPath+"/nas"+prefix+".pdf");
        }
        NasManageEntity nasManageEntity=nasManageMapstruct.toEntity(nasManageDto);
        this.saveNotNull(nasManageEntity);
    }

    /**
    * @description 删除审核信息
    * @author zzj
    * @date 2020/9/7 11:49
    * @return void
    * @throws
    */
    public void deleteIds(String[] ids){
        this.deleteByIds(Arrays.asList(ids));
    }
    /**
    * @description 审核nas存储
    * @author zzj
    * @date 2020/9/7 17:16
    * @return void
    * @throws
    */
    @Transactional
    public void  audit(NasManageDto nasManageDto, ResultT<String> resultT){
        NasManageEntity nasManageEntity=nasManageMapstruct.toEntity(nasManageDto);
        if("1".equals(nasManageDto.getAuditStatus())){
            String[] ips=nasManageDto.getIps().split(";");
            for(int i=0;i<ips.length;i++){
                String ip=ips[i];
                ManagedChannel channel= ManagedChannelBuilder.forTarget("static://"+ip+":"+6577)
                        .usePlaintext()
                        .build();
                NasAccreditServiceGrpc.NasAccreditServiceBlockingStub stub= NasAccreditServiceGrpc.newBlockingStub(channel);
                NasAccredit.NasRequest nasRequest=NasAccredit.NasRequest.newBuilder()
                        .setPath(nasManageDto.getPrivateDirectory()).setUser(nasManageDto.getUserName())
                        .setPermission("1").build();
                NasAccredit.NasResponse nasResponse=stub.handle(nasRequest);
                if(nasResponse.getCode()==0){
                    nasManageEntity.setAuditOpinion(ip+":acl设置成功");
                }else {
                    nasManageEntity.setAuditOpinion(ip+":acl设置失败");
                }
                this.saveNasDetail(nasManageEntity,ip);
                channel.shutdown();

            }

        }
        this.saveNotNull(nasManageEntity);

    }

    public void saveNasDetail(NasManageEntity nasManageEntity,String ip){
        NasDetailsEntity detailsEntity=new NasDetailsEntity();
        detailsEntity.setId(ip+"_"+nasManageEntity.getPrivateDirectory());
        detailsEntity.setIp(ip);
        detailsEntity.setIsDocker(nasManageEntity.getIsDocker());
        detailsEntity.setUserId(nasManageEntity.getUserId());
        detailsEntity.setUserName(nasManageEntity.getUserName());
        nasDetailsDao.saveNotNull(detailsEntity);
        //detailsEntity.
    }
    public void saveNasQuota(NasManageEntity nasManageEntity){
        NasQuotaEntity nasQuotaEntity=new NasQuotaEntity();
        nasQuotaEntity.setId(nasManageEntity.getPrivateDirectory());
        nasQuotaEntity.setNasVendor("华为NAS");
        nasQuotaEntity.setAuditResults(nasManageEntity.getAuditStatus());
        nasQuotaEntity.setAuditTime(new Date());
        nasQuotaEntity.setHardThreshold(nasManageEntity.getHardThreshold());
        nasQuotaEntity.setPrivateDirectory(nasManageEntity.getPrivateDirectory());
        nasQuotaEntity.setResourceStatus("1");
        nasQuotaEntity.setReviewMaterials(nasManageEntity.getReviewMaterials());
        nasQuotaEntity.setReviewMaterialsPdf(nasManageEntity.getReviewMaterialsPdf());

        QuotaEnum quotaEnum=QuotaEnum.match(nasQuotaEntity.getNasVendor(),QuotaEnum.HUAWEI);
        BaseQuota baseQuota=quotaEnum.getBaseQuota();
        ResultT<String> resultT=baseQuota.add(nasQuotaEntity.getPrivateDirectory(),nasManageEntity.getHardThreshold());
        if(resultT.isSuccess()){

        }else {
            nasQuotaEntity.setResourceStatus("-1");
        }
        nasQuotaDao.saveNotNull(nasQuotaEntity);

    }

}
