package com.biyanzhi.parser;

import org.json.JSONObject;

import com.biyanzhi.data.result.Result;
import com.biyanzhi.data.result.SimpleResult;

public class SimpleParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		return new SimpleResult();
	}

}
