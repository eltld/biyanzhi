package com.biyanzhi.task;

import com.biyanzhi.data.Picture;
import com.biyanzhi.enums.RetError;

public class PublishPictureTask extends BaseAsyncTask<Picture, Void, RetError> {
	private Picture pic;

	@Override
	protected RetError doInBackground(Picture... params) {
		pic = params[0];
		RetError ret = pic.publishPicture();
		return ret;
	}

}
