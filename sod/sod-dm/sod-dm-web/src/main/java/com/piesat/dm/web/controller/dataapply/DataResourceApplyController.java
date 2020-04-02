package com.piesat.dm.web.controller.dataapply;

import com.piesat.dm.rpc.api.dataapply.DataResourceApplyService;
import com.piesat.dm.rpc.dto.dataapply.DataResourceApplyDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yaya
 * @description portal 资源申请接口
 * @date 2020/3/30 13:12
 */
@RestController
@RequestMapping("/dm/resourceApply")
@Api(tags = "portal资源申请接口")
public class DataResourceApplyController {

    @Autowired
    private DataResourceApplyService dataResourceApplyService;



    @PostMapping(value = "/save")
    @ApiOperation(value = "添加", notes = "添加")
    public ResultT save(@RequestBody DataResourceApplyDto dataResourceApplyDto) {
        try {
            DataResourceApplyDto save = this.dataResourceApplyService.saveDto(dataResourceApplyDto);
            return ResultT.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
