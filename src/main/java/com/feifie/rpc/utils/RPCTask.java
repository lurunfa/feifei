package com.feifie.rpc.utils;

import com.feifie.rpc.entity.Json;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionOptions;
import net.minidev.json.JSONObject;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author kevin
 * @date 2018/9/12
 * @since 0.1.0
 **/
public class RPCTask implements Callable<Json> {

    private final Map<String,Object> params;

    private final int requestId;

    private final String url;

    private final String method;

    private JSONRPC2SessionOptions options = new JSONRPC2SessionOptions();
    RPCTask(Map<String, Object> params, int requestId, String url, String method) {
        this.params = params;
        this.requestId = requestId;
        this.url = url;
        this.method = method;
    }

    public RPCTask(Map<String, Object> params, int requestId, String url, String method, JSONRPC2SessionOptions options) {
        this.params = params;
        this.requestId = requestId;
        this.url = url;
        this.method = method;
        this.options = options;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Json call() throws Exception {
        URL serverUrl = new URL(url);
        JSONRPC2Session mySession = new JSONRPC2Session(serverUrl);
        mySession.setOptions(options);
        JSONRPC2Request request = new JSONRPC2Request(method,requestId);
        request.setNamedParams(params);
        JSONRPC2Response response = mySession.send(request);
        Json json = new Json();
        if (response.indicatesSuccess()){
            JSONObject result = (JSONObject) response.getResult();
            String s = result.toJSONString();
            json.setObj(s);
            json.setSuccess(true);
            return json;
        }
        else{
            json.setSuccess(false);
            json.setMsg(response.getError().getMessage());
            return json;
        }
    }
}
