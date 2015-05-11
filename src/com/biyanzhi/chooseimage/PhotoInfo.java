package com.biyanzhi.chooseimage;

import java.io.Serializable;

/**
 * 
 * 本地相册图片bean<br>
 * {@link #image_id}图片id<br>
 * {@link #path_absolute} 绝对路径<br>
 * {@link #file_path} 用于显示的路�?br> {@link #choose} 是否被�?�?br>
 */
public class PhotoInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private int image_id;
	private String path_file = "";
	private String path_absolute = "";
	private boolean choose = false;
	private double latitude;// 维度
	private double longitude;// 经度
	private String date = "";

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getImage_id() {
		return image_id;
	}

	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}

	public String getPath_file() {
		return path_file;
	}

	public void setPath_file(String path_file) {
		this.path_file = path_file;
	}

	public String getPath_absolute() {
		return path_absolute;
	}

	public void setPath_absolute(String path_absolute) {
		this.path_absolute = path_absolute;
	}

	public boolean isChoose() {
		return choose;
	}

	public void setChoose(boolean choose) {
		this.choose = choose;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "path:" + path_file + "   date:" + date;
	}
}
