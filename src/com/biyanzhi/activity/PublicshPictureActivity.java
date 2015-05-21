package com.biyanzhi.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.biyanzhi.R;
import com.biyanzhi.activity.ImageDelPagerActivity.DelPic;
import com.biyanzhi.data.Picture;
import com.biyanzhi.enums.RetError;
import com.biyanzhi.popwindow.SelectPicPopwindow;
import com.biyanzhi.popwindow.SelectPicPopwindow.SelectOnclick;
import com.biyanzhi.task.PublishPictureTask;
import com.biyanzhi.utils.Constants;
import com.biyanzhi.utils.DialogUtil;
import com.biyanzhi.utils.FileUtils;
import com.biyanzhi.utils.ToastUtil;
import com.biyanzhi.view.RoundAngleImageView;
import com.biyianzhi.interfaces.AbstractTaskPostCallBack;

public class PublicshPictureActivity extends BaseActivity implements
		SelectOnclick, DelPic {
	private Button btnPublish;
	private EditText content;// 内容输入框
	private TextView txt_title;
	private ImageView back;
	private RoundAngleImageView image;

	private SelectPicPopwindow pop;

	private String cameraPath = "";
	private Dialog dialog;

	private String image_path = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publish_picture_activity);
		initView();
	}

	private void initView() {
		image = (RoundAngleImageView) findViewById(R.id.img);
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("上传照片");
		btnPublish = (Button) findViewById(R.id.btnUpload);
		content = (EditText) findViewById(R.id.txt_content);
		setListener();
	}

	private void setListener() {
		image.setOnClickListener(this);
		btnPublish.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.REQUEST_CODE_GETIMAGE_BYSDCARD) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				if (cursor != null) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						String path = cursor.getString(column_index);
						Bitmap bitmap = BitmapFactory.decodeFile(path);
						if (null == bitmap) {
							return;
						}
						image.setImageBitmap(bitmap);
						image_path = path;

					}
				}

			}
		}
		// 拍摄图片
		else if (requestCode == Constants.REQUEST_CODE_GETIMAGE_BYCAMERA) {
			if (resultCode != RESULT_OK) {
				return;
			}
			File file = new File(cameraPath);
			if (!file.exists()) {
				// ToastUtil.showToast("图片获取失败，请重新获取", Toast.LENGTH_SHORT);
				return;
			}

			// photoPathLists.add(photoPathLists.size() - 1, cameraPath);
		}
	}

	private void publishPic(Picture picture) {
		dialog = DialogUtil.createLoadingDialog(this);
		dialog.show();
		PublishPictureTask task = new PublishPictureTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result == RetError.NONE) {
					finishThisActivity();
				}
			}
		});
		task.executeParallel(picture);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img:
			if ("".equals(image_path)) {
				pop = new SelectPicPopwindow(this, v, "拍照", "从相册选择");
				pop.setmSelectOnclick(this);
				pop.show();
			} else {
				List<String> imgUrl = new ArrayList<String>();
				imgUrl.add(image_path);
				Intent intent = new Intent(this, ImageDelPagerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
						(Serializable) imgUrl);
				intent.putExtras(bundle);
				intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 0);
				intent.putExtra("type", 1);
				startActivity(intent);
				ImageDelPagerActivity.setCallBack(this);
			}
			break;
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.btnUpload:
			String str_content = content.getText().toString().trim();
			if (str_content.length() == 0) {
				ToastUtil.showToast("随便说点啥吧");
				return;
			}
			Picture picture = new Picture();
			picture.setContent(str_content);
			picture.setPicture_image_url(image_path);
			publishPic(picture);
			break;
		default:
			break;
		}
	}

	@Override
	public void menu1_select() {
		String name = FileUtils.getFileName() + ".jpg";
		cameraPath = FileUtils.getCameraPath() + File.separator + name;
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(cameraPath)));
		startActivityForResult(intent, Constants.REQUEST_CODE_GETIMAGE_BYCAMERA);
	}

	@Override
	public void menu2_select() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, Constants.REQUEST_CODE_GETIMAGE_BYSDCARD);

	}

	@Override
	public void del(int position) {
		image.setImageResource(R.drawable.add_pic);
		image_path = "";
	}
}
