package com.biyanzhi.parser;

import org.json.JSONObject;

import com.biyanzhi.data.result.Result;

public interface IParser {
	public Result parse(JSONObject jsonObj) throws Exception;
}
