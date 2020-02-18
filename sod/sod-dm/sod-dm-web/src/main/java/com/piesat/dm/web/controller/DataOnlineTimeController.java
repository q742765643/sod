package com.piesat.dm.web.controller;

import com.piesat.common.utils.StringUtils;
import com.piesat.dm.rpc.api.DataOnlineTimeService;
import com.piesat.dm.rpc.dto.DataOnlineTimeDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResultT<PageBean> onLineList(HttpServletRequest request,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Map<String,String> map = new HashMap<String,String>();
        if(StringUtils.isNotNullString(request.getParameter("class_name"))){
            map.put("class_name",request.getParameter("class_name"));
        }
        if(StringUtils.isNotNullString(request.getParameter("d_data_id"))){
            map.put("d_data_id",request.getParameter("d_data_id"));
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
