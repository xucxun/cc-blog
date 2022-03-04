package com.example.blog.util;

import ch.qos.logback.core.status.ErrorStatus;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BlogUtil {

    //生成随机字符串,32位
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密，加盐，生成密文
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJsonResult(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJsonResult(int code, String msg) {
        return getJsonResult(code, msg, null);
    }

    public static String getJsonResult(int code) {
        return getJsonResult(code, null, null);
    }

}
