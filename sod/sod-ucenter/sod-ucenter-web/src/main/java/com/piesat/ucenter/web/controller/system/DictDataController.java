package com.piesat.ucenter.web.controller.system;

import com.piesat.ucenter.rpc.api.system.DictDataService;
import com.piesat.ucenter.rpc.dto.system.DictDataDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/3 18:17
 */
@RestController
@RequestMapping("/system/dict/data")
public class DictDataController {
    @Autowired
    private DictDataService dictDataService;
    @GetMapping("/list")
    public ResultT<PageBean> list(DictDataDto dictData,int pageNum,int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT();
        PageForm<DictDataDto> pageForm=new PageForm<>(pageNum,pageSize,dictData);
        PageBean pageBean=dictDataService.selectDictDataList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }
    /**
     * 查询字典数据详细
     */
    @GetMapping(value = "/{dictCode}")
    public ResultT<DictDataDto> getInfo(@PathVariable String dictCode)
    {
        ResultT<DictDataDto> resultT=new ResultT<>();
        DictDataDto dictDataDto=dictDataService.selectDictDataById(dictCode);
        resultT.setData(dictDataDto);
        return resultT;
    }
    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/dictType/{dictType}")
    public ResultT<List<DictDataDto>> dictType(@PathVariable String dictType)
    {
        ResultT<List<DictDataDto>> resultT=new ResultT<>();
        List<DictDataDto> dictDataDtoList=dictDataService.selectDictDataByType(dictType);
        resultT.setData(dictDataDtoList);
        return resultT;
    }
}
