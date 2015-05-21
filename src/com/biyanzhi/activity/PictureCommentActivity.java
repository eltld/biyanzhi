package com.biyanzhi.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biyanzhi.R;
import com.biyanzhi.data.Picture;
import com.biyanzhi.data.PictureScore;
import com.biyanzhi.enums.RetError;
import com.biyanzhi.showbigimage.ImagePagerActivity;
import com.biyanzhi.task.SendPictureScoreTask;
import com.biyanzhi.utils.Constants;
import com.biyanzhi.utils.DialogUtil;
import com.biyanzhi.utils.UniversalImageLoadTool;
import com.biyanzhi.utils.Utils;
import com.biyianzhi.interfaces.AbstractTaskPostCallBack;
import com.biyianzhi.interfaces.ConfirmDialog;

import fynn.app.PromptDialog;

public class PictureCommentActivity extends BaseActivity implements
		OnClickListener, TextWatcher {
	private ImageView img_avatar;
	private TextView txt_user_name;
	private TextView txt_time;
	private TextView txt_context;
	private ImageView img;
	private ImageView back;
	private Button btnComment;
	private EditText edit_comment;
	private Dialog dialog;

	private int position;

	private boolean isReplaySomeOne = false;

	private int replaySomeOneID = 0;
	private String replaySomeOneName = "";

	private LinearLayout comment_layout;

	private RelativeLayout layout_title;

	private Picture picture;

	private RatingBar ratingBar;
	private TextView txt_score;

	private boolean autoChange;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		picture = (Picture) getIntent().getSerializableExtra("picture");
		initView();
		setValue();
	}

	private void initView() {
		layout_title = (RelativeLayout) findViewById(R.id.layout_title);
		back = (ImageView) findViewById(R.id.back);
		img = (ImageView) findViewById(R.id.img);
		txt_context = (TextView) findViewById(R.id.txt_content);
		txt_time = (TextView) findViewById(R.id.txt_time);
		txt_user_name = (TextView) findViewById(R.id.txt_user_name);
		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		btnComment = (Button) findViewById(R.id.btnComment);
		edit_comment = (EditText) findViewById(R.id.edit_content);
		comment_layout = (LinearLayout) findViewById(R.id.layout_comment);
		ratingBar = (RatingBar) findViewById(R.id.ratingbar);
		txt_score = (TextView) findViewById(R.id.txt_score);
		LayoutParams layoutParams = img.getLayoutParams();
		layoutParams.width = Utils.getSecreenWidth(this) - 100;
		img.setLayoutParams(layoutParams);
		setListener();
	}

	private void setListener() {
		img.setOnClickListener(this);
		back.setOnClickListener(this);
		btnComment.setOnClickListener(this);
		edit_comment.addTextChangedListener(this);
		Utils.getFocus(layout_title);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				if (!autoChange) {
					autoChange = true;
					return;
				}
				txt_score.setText((int) (arg1 * 20) + "(分)");
				showScore((int) (arg1 * 20));
			}
		});
	}

	private void setValue() {
		String content = picture.getContent();
		if ("".equals(content)) {
			txt_context.setVisibility(View.GONE);
		} else {
			txt_context.setText(picture.getContent());
		}
		txt_time.setText(picture.getPublish_time());
		UniversalImageLoadTool.disPlay(picture.getPicture_image_url(), img,
				R.drawable.empty_photo);
		if (picture.getAverage_score() != 0) {
			autoChange = false;
			ratingBar.setRating((float) picture.getAverage_score() / 20);
			txt_score.setText(picture.getAverage_score() + "(分)");
		} else {
			autoChange = true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.btnComment:
			String content = edit_comment.getText().toString().trim();
			if (content.length() == 0) {
				return;
			}
			break;
		case R.id.img:
			List<String> imgUrl = new ArrayList<String>();
			imgUrl.add(picture.getPicture_image_url());
			Intent intent = new Intent(this, ImagePagerActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
					(Serializable) imgUrl);
			intent.putExtras(bundle);
			intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 1);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void delReplaySomeOne() {
		isReplaySomeOne = false;
		replaySomeOneID = 0;
		replaySomeOneName = "";
	}

	@Override
	public void afterTextChanged(Editable str) {
		if (isReplaySomeOne) {
			if (replaySomeOneName.equals(str.toString().replace("@", ""))) {
				edit_comment.setText("");
				delReplaySomeOne();
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	private void showScore(final int score) {
		String str = "";
		if (score >= 80) {
			str = score + "分 你给TA打的分数很高哦,我猜TA是个美女(帅哥)";
		} else {
			str = score + "分 看来TA不是你的菜,分数不够高哦";

		}
		PromptDialog.Builder dialog = DialogUtil.confirmDialog(this, str, "确定",
				null, new ConfirmDialog() {

					@Override
					public void onOKClick() {
						sendScore(score);
					}

					public void onCancleClick() {

					}
				});
		dialog.show();
	}

	private void sendScore(int score) {
		SendPictureScoreTask task = new SendPictureScoreTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
			}
		});
		PictureScore picscore = new PictureScore();
		picscore.setPicture_id(picture.getPicture_id());
		picscore.setPicture_score(score);
		task.executeParallel(picscore);
	}
}
