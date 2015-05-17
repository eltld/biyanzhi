package com.biyanzhi.task;

import com.biyanzhi.data.PictureScore;
import com.biyanzhi.enums.RetError;

public class SendPictureScoreTask extends
		BaseAsyncTask<PictureScore, Void, RetError> {
	private PictureScore score;

	@Override
	protected RetError doInBackground(PictureScore... params) {
		score = params[0];
		return score.sendScore();
	}

}
