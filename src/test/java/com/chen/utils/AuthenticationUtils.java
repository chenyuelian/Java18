package com.chen.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONPath;

/**鉴权工具类
 * @author 曦月女孩
 *
 */
public class AuthenticationUtils {
	//定义一个环境变量
public static final Map<String,Object> ENV = new HashMap<>();



/**
 * 使用jsonpath表达式获取值，存储到环境变量中
 * @param json          json字符串
 * @param expression    jsonpath表达式
 * @param key            存储key
 */
public static void getValue2ENV(String json,String expression,String key) {
	Object object = JSONPath.read(json, expression);
	if(object != null) {
		AuthenticationUtils.ENV.put(key,object);
	}
}

}
