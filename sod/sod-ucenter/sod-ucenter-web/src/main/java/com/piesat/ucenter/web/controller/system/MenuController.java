package com.piesat.ucenter.web.controller.system;

import com.piesat.ucenter.entity.system.MenuEntity;
import com.piesat.ucenter.rpc.api.system.MenuService;
import com.piesat.ucenter.rpc.dto.system.MenuDto;
import com.piesat.ucenter.rpc.util.TreeSelect;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/29 14:46
 */
@RestController
@RequestMapping("/system/menu")
@Api(value="菜单controller",tags={"菜单操作接口"})
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResultT<List<MenuDto>> list(MenuDto menu)
    {
        ResultT<List<MenuDto>> resultT=new ResultT<>();
        List<MenuDto> menuDtos=menuService.selectMenuList(menu);
        resultT.setData(menuDtos);
        return resultT;
    }
    @GetMapping("/treeselect")
    public ResultT<List<TreeSelect>> treeselect(MenuDto menu)
    {
        ResultT< List<TreeSelect>> resultT=new ResultT<>();
        List<TreeSelect> treeSelects = menuService.treeSelect(menu);
        resultT.setData(treeSelects);
        return resultT;
    }
    /**
     * 新增菜单
     */

    @PostMapping
    public ResultT<MenuDto> add(@RequestBody MenuDto menu)
    {
        ResultT<MenuDto> resultT=new ResultT<>();
        MenuDto menuDto=menuService.insertMenu(menu);
        resultT.setData(menuDto);
        return resultT;
    }
    /**
     * 修改菜单
     */
    @PutMapping
    public ResultT<MenuDto> edit(@RequestBody MenuDto menu)
    {
        ResultT<MenuDto> resultT=new ResultT<>();
        MenuDto menuDto=menuService.insertMenu(menu);
        resultT.setData(menuDto);
        return resultT;
    }
    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "/{menuId}")
    public ResultT<MenuDto> getInfo(@PathVariable String menuId)
    {
        ResultT<MenuDto> resultT=new ResultT<>();
        MenuDto menuDto=menuService.selectMenuById(menuId);
        resultT.setData(menuDto);
        return resultT;
    }


}
