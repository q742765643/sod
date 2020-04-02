package com.piesat.dm.web.controller.dataclass;

import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.dataclass.DataOnlineTimeService;
import com.piesat.dm.rpc.dto.dataclass.DataOnlineTimeDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/18 11:04
 */
@Api(tags = "在线时间检索")
@RestController
@RequestMapping("/dm/onlineTime")
public class DataOnlineTimeController {


    @Autowired
    private DataOnlineTimeService dataOnlineTimeService;

    @GetMapping("/onLineList")
    @ApiOperation(value = "在线时间检索条件分页查询", notes = "在线时间检索条件分页查询")
    public ResultT<PageBean> onLineList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, String class_name, String d_data_id) {
        Map<String,String> map = new HashMap<String,String>();
        if(StringUtils.isNotNullString(class_name)){
            map.put("class_name",class_name);
        }
        if(StringUtils.isNotNullString(d_data_id)){
            map.put("d_data_id",d_data_id);
        }
        ResultT<PageBean> resultT = new ResultT<>();
        PageForm<Map<String,String>> pageForm = new PageForm<>(pageNum, pageSize, map);
        PageBean pageBean = dataOnlineTimeService.onLineList(pageForm);
        resultT.setData(pageBean);
        return resultT;
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改资料在线时间", notes = "修改资料在线时间")
    public ResultT<String> update(@RequestBody DataOnlineTimeDto dataOnlineTimeDto)
    {
        ResultT<String> resultT=new ResultT<>();
        dataOnlineTimeService.update(dataOnlineTimeDto);
        return resultT;
    }
}
