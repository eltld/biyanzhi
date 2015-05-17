package com.biyanzhi.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.biyanzhi.R;
import com.biyanzhi.adapter.StaggeredAdapter;
import com.biyanzhi.data.Picture;
import com.biyanzhi.data.PictureList;
import com.biyanzhi.enums.RetError;
import com.biyanzhi.popwindow.SelectPicPopwindow.SelectOnclick;
import com.biyanzhi.task.GetPictureListTask;
import com.biyanzhi.utils.Constants;
import com.biyanzhi.utils.DialogUtil;
import com.biyanzhi.utils.FileUtils;
import com.biyanzhi.utils.Utils;
import com.biyianzhi.interfaces.AbstractTaskPostCallBack;
import com.etsy.android.grid.StaggeredGridView;

public class MainActivity extends BaseActivity implements SelectOnclick {
	private StaggeredAdapter mAdapter = null;
	private ImageView img_select;
	private String cameraPath = "";
	private Dialog dialog;
	private List<Picture> mLists = new ArrayList<Picture>();
	private PictureList list = new PictureList();
	private StaggeredGridView mGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setValue();
		getPictureList();
	}

	private void initView() {
		img_select = (ImageView) findViewById(R.id.img_create);
		img_select.setOnClickListener(this);
		mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
	}

	private void setValue() {
		mAdapter = new StaggeredAdapter(this, mLists);
		mGridView.setAdapter(mAdapter);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_create:
			startActivity(new Intent(this, PublicshPictureActivity.class));
			Utils.leftOutRightIn(this);
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

	private void getPictureList() {
		dialog = DialogUtil.createLoadingDialog(this);
		dialog.show();
		GetPictureListTask task = new GetPictureListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				mLists.addAll(list.getPictureList());
				mAdapter.notifyDataSetChanged();
			}
		});
		task.executeParallel(list);
	}

}

// end of class
// public class MainActivity extends BaseActivity {
// private PtrClassicFrameLayout mPtrFrame;
//
// @Override
// protected void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// setContentView(R.layout.activity_main);
// initView();
// }
//
// private void initView() {
// initFefushView();
// }
//
// private void initFefushView() {
// mPtrFrame = (PtrClassicFrameLayout)
// findViewById(R.id.rotate_header_grid_view_frame);
// mPtrFrame.setLastUpdateTimeRelateObject(this);
// mPtrFrame.setPtrHandler(new PtrHandler() {
// @Override
// public void onRefreshBegin(PtrFrameLayout frame) {
// mPtrFrame.refreshComplete();
// }
//
// @Override
// public boolean checkCanDoRefresh(PtrFrameLayout frame,
// View content, View header) {
// return PtrDefaultHandler.checkContentCanBePulledDown(frame,
// content, header);
// }
// });
// // the following are default settings
// mPtrFrame.setResistance(1.7f);
// mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
// mPtrFrame.setDurationToClose(200);
// mPtrFrame.setDurationToCloseHeader(1000);
// // default is false
// mPtrFrame.setPullToRefresh(false);
// // default is true
// mPtrFrame.setKeepHeaderWhenRefresh(true);
// mPtrFrame.postDelayed(new Runnable() {
// @Override
// public void run() {
// // mPtrFrame.autoRefresh();
// }
// }, 100);
// }
// }
