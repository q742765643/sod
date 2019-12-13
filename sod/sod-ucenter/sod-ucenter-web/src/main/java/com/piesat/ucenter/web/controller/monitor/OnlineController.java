package com.piesat.ucenter.web.controller.monitor;

import com.piesat.ucenter.rpc.api.monitor.OnlineService;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/9 14:47
 */
@RestController
@RequestMapping("/monitor/online")
public class OnlineController {
    @Autowired
    private OnlineService onlineService;
    @GetMapping("/list")
    public ResultT<PageBean> list(String ipaddr, String userName,
                                  @RequestParam(value="pageNum",defaultValue="0") int pageNum,
                                  @RequestParam(value="pageSize",defaultValue="10") int pageSize){
        ResultT<PageBean> resultT=new ResultT<>();
        PageBean pageBean=onlineService.list(ipaddr,userName,pageNum,pageSize);
        resultT.setData(pageBean);
        return resultT;
    }

    /**
     * 强退用户
     */
    @DeleteMapping("/{tokenId}")
    public ResultT<String> forceLogout(@PathVariable String tokenId)
    {
        ResultT<String> resultT=new ResultT<>();
        onlineService.forceLogout(tokenId);
        return resultT;
    }
}
