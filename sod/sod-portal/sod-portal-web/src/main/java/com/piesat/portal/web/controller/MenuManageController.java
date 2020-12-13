package com.piesat.portal.web.controller;

import com.piesat.portal.rpc.api.MenuManageService;
import com.piesat.portal.rpc.dto.MenuManageDto;
import com.piesat.portal.rpc.util.PortalTreeSelect;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 */
@Api(value="菜单管理",tags= {"菜单管理"})
@RequestMapping("/portal/menuManage")
@RestController
public class MenuManageController {

    @Autowired
    private MenuManageService menuManageService;
    @GetMapping("/list")
    public ResultT<List<MenuManageDto>> list(MenuManageDto menuManageDto)
    {
        ResultT<List<MenuManageDto>> resultT=new ResultT<>();
        List<MenuManageDto> menuDtos=menuManageService.selectMenuList(menuManageDto);
        resultT.setData(menuDtos);
        return resultT;
    }

    @GetMapping("/treeselect")
    public ResultT<List<PortalTreeSelect>> treeselect(MenuManageDto menuManageDto)
    {
        ResultT< List<PortalTreeSelect>> resultT=new ResultT<>();
        List<PortalTreeSelect> treeSelects = menuManageService.treeSelect(menuManageDto);
        resultT.setData(treeSelects);
        return resultT;
    }

    /**
     * 新增菜单
     */
    @PostMapping("/addMenu")
    public ResultT<MenuManageDto> add(@RequestBody MenuManageDto menu)
    {
        ResultT<MenuManageDto> resultT=new ResultT<>();
        MenuManageDto menuDto=menuManageService.insertMenu(menu);
        resultT.setData(menuDto);
        return resultT;
    }
    /**
     * 修改菜单
     */
    @PutMapping("/updateMenu")
    public ResultT edit(@RequestBody MenuManageDto menu)
    {
        ResultT<MenuManageDto> resultT=new ResultT<>();
        MenuManageDto menuManageDto = menuManageService.updateMenu(menu);
        resultT.setData(menuManageDto);
        return resultT;
    }

    @GetMapping(value = "/{menuId}")
    public ResultT<MenuManageDto> getInfo(@PathVariable String menuId)
    {
        ResultT<MenuManageDto> resultT=new ResultT<>();
        MenuManageDto menuManageDto = menuManageService.selectMenuById(menuId);
        resultT.setData(menuManageDto);
        return resultT;
    }

    /**
     * 删除菜单
     */
    @ApiOperation(value = "根据id删除")
    @DeleteMapping("/delMenu")
    public ResultT<String> remove(String menuId)
    {
        ResultT<String> resultT=new ResultT<>();
        menuManageService.deleteMenuById(menuId);
        return resultT;
    }

}
