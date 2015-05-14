package com.biyanzhi.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biyanzhi.data.result.ApiRequest;
import com.biyanzhi.data.result.Result;
import com.biyanzhi.enums.RetError;
import com.biyanzhi.enums.RetStatus;
import com.biyanzhi.parser.IParser;
import com.biyanzhi.parser.PictureListParser;

public class PictureList {
	private static final String GET_PICTURELIST_URL = "getpicturelists.do";
	private List<Picture> pictureList = new ArrayList<Picture>();

	public void setPictureList(List<Picture> pictureList) {
		this.pictureList = pictureList;
	}

	public List<Picture> getPictureList() {
		return pictureList;
	}

	public RetError getPictureListFromServer() {
		IParser parser = new PictureListParser();
		Map<String, Object> params = new HashMap<String, Object>();
		Result ret = ApiRequest.request(GET_PICTURELIST_URL, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			PictureList lists = (PictureList) ret.getData();
			this.pictureList = lists.getPictureList();
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}
}
