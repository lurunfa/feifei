package com.feifie.rpc.controller;

import com.feifie.rpc.entity.Json;
import com.feifie.rpc.utils.RPCTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author kevin
 * @date 2018/12/9
 * @since 0.1.0
 **/
@RestController
@Slf4j
public class IndexController {

    @Resource
    private RPCTools rpcTools;

    /**
     * 测试首页
     * @return
     */
    @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("index.html");
    }

    /**
     * //todo 可修改
     * @param method 方法名
     * @param obj Obj
     * @return
     */
    @PostMapping("/receive")
    public Json receive(String method,String obj){
        Future<Json> send = rpcTools.send(obj, method);
        try {
            return send.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("get failed",e);
            Json res = new Json();
            res.setSuccess(false);
            res.setMsg("failed");
            return res;
        }
    }
}
