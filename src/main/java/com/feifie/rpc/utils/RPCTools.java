package com.feifie.rpc.utils;

import com.feifie.rpc.entity.Json;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RPC通讯工具类
 *
 * @author kevin
 * @date 2018/9/11
 * @since 0.1.0
 **/
@Component("rpcTools")
public class RPCTools {


    private AtomicInteger atomicInteger = new AtomicInteger();

    private JSONRPC2SessionOptions defaultOptions;

    @Value("${rpc.connectTimeout}")
    private int connectTimeout;

    @Value("${rpc.readTimeout}")
    private int readTimeout;


    private static final String METHOD_NAME_NOT_NULL = "METHOD_NAME_NOT_NULL";

    @Value("${rpc.defaultUrl}")
    private String defaultUrl;
    @Value("${rpc.workThreadNum}")
    private  int workThreadNum;
    @Value("${rpc.maxWorkThreadNum}")
    private  int maxWorkThreadNum;
    @Value("${rpc.keepAliveTime}")
    private long keepAliveTime;
    @Value("${rpc.maxQueueSize}")
    private int maxQueueSize;
    private ThreadPoolExecutor threadPoolExecutor;


    @PostConstruct
    private void init(){
        defaultOptions = new JSONRPC2SessionOptions();
        defaultOptions.setConnectTimeout(connectTimeout);
        defaultOptions.setReadTimeout(readTimeout);
        threadPoolExecutor = new ThreadPoolExecutor(workThreadNum, maxWorkThreadNum, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>(maxQueueSize),new ThreadFactoryBuilder().setNameFormat("rpc-runner-%d").build());
    }

    public Future<Json> send(Map<String,Object> params, String method, String url) {
        if (method == null||"".equals(method)){
            throw new NullPointerException(METHOD_NAME_NOT_NULL);
        }
        if (url == null||"".equals(url)){
            url = defaultUrl;
        }
        return threadPoolExecutor.submit(new RPCTask(params, atomicInteger.incrementAndGet(), url, method,defaultOptions));
    }
    public Future<Json> send(String json, String method) {
        Map<String,Object> params = new HashMap<>();
        params.put("json",json);
        if (method == null||"".equals(method)){
            throw new NullPointerException(METHOD_NAME_NOT_NULL);
        }
        return threadPoolExecutor.submit(new RPCTask(params, atomicInteger.incrementAndGet(), defaultUrl, method,defaultOptions));
    }

}
