package com.biyanzhi.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.biyanzhi.data.Picture;
import com.biyanzhi.data.PictureImage;
import com.biyanzhi.data.PictureList;
import com.biyanzhi.data.result.Result;

public class PictureListParser implements IParser {

	@Override
	public Result<PictureList> parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		JSONArray jsonArr = jsonObj.getJSONArray("pictures");
		if (jsonArr == null) {
			return Result.defContentErrorResult();
		}
		List<Picture> lists = new ArrayList<Picture>();
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			int picture_id = obj.getInt("picture_id");
			int publisher_id = obj.getInt("publisher_id");
			String publisher_name = obj.getString("publisher_name");
			String publisher_avatar = obj.getString("publisher_avatar");
			String publish_time = obj.getString("publish_time");
			String content = obj.getString("content");
			String picture_image_url = obj.getString("picture_image_url");
			int average_score = obj.getInt("average_score");
			// JSONArray jsonImages = obj.getJSONArray("images");
			// List<PictureImage> images = new ArrayList<PictureImage>();
			// for (int j = 0; j < jsonImages.length(); j++) {
			// JSONObject obj2 = (JSONObject) jsonImages.opt(j);
			// int image_id = obj2.getInt("image_id");
			// String image_url = obj2.getString("image_url");
			// PictureImage pimg = new PictureImage();
			// pimg.setImage_id(image_id);
			// pimg.setImage_url(image_url);
			// images.add(pimg);
			// }
			Picture picture = new Picture();
			picture.setContent(content);
			// picture.setImages(images);
			picture.setPicture_id(picture_id);
			picture.setPublish_time(publish_time);
			picture.setPublisher_avatar(publisher_avatar);
			picture.setPublisher_id(publisher_id);
			picture.setPublisher_name(publisher_name);
			picture.setPicture_image_url(picture_image_url);
			picture.setAverage_score(average_score);
			lists.add(picture);
		}
		PictureList cl = new PictureList();
		cl.setPictureList(lists);
		Result<PictureList> ret = new Result<PictureList>();
		ret.setData(cl);
		return ret;
	}
}
