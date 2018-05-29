package com.baidan.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by baidan on 2018/4/2.
 */
public class ResultCode {

    public static Map<String,Object> getResultCode(String code,Map data){
        Map<String,Object> resultCode = new HashMap<String, Object>();
        if(code == null){
            resultCode.put("msg","code不能为空！");
        }
        if(code == "-3003"){
            resultCode.put("msg","操作失败！");
        }
        if(code == "0000"){
            resultCode.put("msg","操作成功");
        }
        resultCode.put("code", code);
        resultCode.put("data", data);

        return  resultCode;
    }
}
