package com.piesat.dm.web.controller;

import com.piesat.dm.rpc.service.ImportData;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据导入
 *
 * @author cwh
 * @date 2020年 01月17日 16:42:08
 */
@Api(tags = "数据导入专用（禁止使用）")
@RequestMapping("/dm/import")
@RestController
public class ImportDataController {
    @Autowired
    private ImportData importData;

    @ApiOperation(value = "数据导入专用（禁止使用）")
    @PostMapping(value = "/import")
    public ResultT save() {
        try {
//            this.importData.importDatumData();
            return ResultT.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultT.failed(e.getMessage());
        }
    }

}
