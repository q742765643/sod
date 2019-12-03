package com.piesat.ucenter.web.controller.system;

import com.piesat.ucenter.rpc.api.system.DictTypeService;
import com.piesat.ucenter.rpc.dto.system.DictTypeDto;
import com.piesat.ucenter.rpc.dto.system.UserDto;
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
 * @创建时间 2019/12/3 16:10
 */
@RestController
@RequestMapping("/system/dict/type")
public class DictTypeController {
    @Autowired
    private DictTypeService dictTypeService;
    @GetMapping("/list")
    public ResultT<PageBean> list(DictTypeDto dictType,int pageNum,int pageSize)
    {
        ResultT<PageBean> resultT=new ResultT<>();
        PageForm<DictTypeDto> pageForm=new PageForm<>(pageNum,pageSize,dictType);
        PageBean pageBean=dictTypeService.selectDictTypeList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }
    /**
     * 查询字典类型详细
     */
    @GetMapping(value = "/{dictId}")
    public ResultT<DictTypeDto> getInfo(@PathVariable String dictId)
    {
        ResultT<DictTypeDto> resultT=new ResultT<>();
        DictTypeDto dictTypeDto=dictTypeService.selectDictTypeById(dictId);
        resultT.setData(dictTypeDto);
        return resultT;
    }
}
