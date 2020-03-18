package com.mirth.jsonpath;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class Core {

	public static String read(String json, String path) {
		return JsonPath.parse(json).read(path);
	}

	public static String update(String json, String key, String value) {
		DocumentContext ctx = JsonPath.parse(json);
		ctx = ctx.set(key, value);
		return ctx.jsonString();
	}

	public static String delete(String json, String path) {
		DocumentContext ctx = JsonPath.parse(json);
		ctx = ctx.delete(path);
		return ctx.jsonString();
	}

	public static String insertObject(String json, String path, String key, Object value) {
		DocumentContext ctx = JsonPath.parse(json);
		ctx = ctx.put(path, key, JsonPath.parse(value).json());
		return ctx.jsonString();
	}
	
	public static String insert(String json, String path, String key, String value) {
		DocumentContext ctx = JsonPath.parse(json);
		ctx = ctx.put(path, key, value);
		return ctx.jsonString();
	}

}
