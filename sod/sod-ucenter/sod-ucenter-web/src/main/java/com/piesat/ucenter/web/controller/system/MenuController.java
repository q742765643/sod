package com.piesat.ucenter.web.controller.system;

import com.piesat.ucenter.rpc.api.system.MenuService;
import com.piesat.ucenter.rpc.dto.system.MenuDto;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
