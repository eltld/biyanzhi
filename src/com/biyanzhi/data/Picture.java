package com.biyanzhi.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biyanzhi.data.result.ApiRequest;
import com.biyanzhi.data.result.Result;
import com.biyanzhi.enums.RetError;
import com.biyanzhi.enums.RetStatus;
import com.biyanzhi.parser.IParser;
import com.biyanzhi.parser.SimpleParser;
import com.biyanzhi.utils.BitmapUtils;

public class Picture {
	private static final String PUBLISH_PICTURE = "addpicture.do";
	private int picture_id;
	private int publisher_id = 0;
	private String publish_time = "";
	private String content = "";
	private String publisher_name = "";
	private String publisher_avatar = "";
	private List<PictureImage> images = new ArrayList<PictureImage>();

	public int getPicture_id() {
		return picture_id;
	}

	public void setPicture_id(int picture_id) {
		this.picture_id = picture_id;
	}

	public int getPublisher_id() {
		return publisher_id;
	}

	public void setPublisher_id(int publisher_id) {
		this.publisher_id = publisher_id;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPublisher_name() {
		return publisher_name;
	}

	public void setPublisher_name(String publisher_name) {
		this.publisher_name = publisher_name;
	}

	public String getPublisher_avatar() {
		return publisher_avatar;
	}

	public void setPublisher_avatar(String publisher_avatar) {
		this.publisher_avatar = publisher_avatar;
	}

	public List<PictureImage> getImages() {
		return images;
	}

	public void setImages(List<PictureImage> images) {
		this.images = images;
	}

	public RetError publishPicture() {
		List<File> bytesimg = new ArrayList<File>();
		for (PictureImage img : this.images) {
			File file = BitmapUtils.getImageFile(img.getImage_url());
			if (file == null) {
				continue;
			}
			bytesimg.add(file);
		}
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("publisher_id", publisher_id);
		params.put("publish_time", publish_time);
		params.put("content", content);
		params.put("publisher_name", publisher_name);
		params.put("publisher_avatar", publisher_avatar);
		Result ret = ApiRequest.uploadFileArrayWithToken(PUBLISH_PICTURE,
				params, bytesimg, parser);
		delImgFile(bytesimg);
		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	private void delImgFile(List<File> files) {
		for (File file : files) {
			if (file.exists()) {
				file.delete();
			}
		}
	}
}
