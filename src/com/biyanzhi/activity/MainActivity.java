package com.biyanzhi.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.biyanzhi.R;
import com.biyanzhi.adapter.StaggeredAdapter;
import com.biyanzhi.chooseimage.SelectPhotoActivity;
import com.biyanzhi.data.Picture;
import com.biyanzhi.data.PictureImage;
import com.biyanzhi.data.PictureList;
import com.biyanzhi.enums.RetError;
import com.biyanzhi.popwindow.SelectPicPopwindow;
import com.biyanzhi.popwindow.SelectPicPopwindow.SelectOnclick;
import com.biyanzhi.task.GetPictureListTask;
import com.biyanzhi.task.PublishPictureTask;
import com.biyanzhi.utils.Constants;
import com.biyanzhi.utils.DialogUtil;
import com.biyanzhi.utils.FileUtils;
import com.biyianzhi.interfaces.AbstractTaskPostCallBack;
import com.etsy.android.grid.StaggeredGridView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;

public class MainActivity extends BaseActivity implements IXListViewListener,
		SelectOnclick {
	// private XListView mAdapterView = null;
	private StaggeredAdapter mAdapter = null;
	private int currentPage = 0;
	private SelectPicPopwindow pop;
	private ImageView img_select;
	private String cameraPath = "";
	private Dialog dialog;
	private List<Picture> mLists = new ArrayList<Picture>();
	private PictureList list = new PictureList();
	private StaggeredGridView mGridView;

	/**
	 * 添加内容
	 * 
	 * @param pageindex
	 * @param type
	 *            1为下拉刷新 2为加载更多
	 */
	private void AddItemToContainer(int pageindex, int type) {

	}

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

		// mAdapterView = (XListView) findViewById(R.id.list);
		// mAdapterView.setPullLoadEnable(true);
		// mAdapterView.setXListViewListener(this);
		// mAdapterView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(PLA_AdapterView<?> parent, View view,
		// int position, long id) {
		//
		// }
		// });
		// mAdapterView.setSelector(R.drawable.list_item_bg);
	}

	private void setValue() {
		mAdapter = new StaggeredAdapter(this, mLists);
		mGridView.setAdapter(mAdapter);
		// mAdapterView.setAdapter(mAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onRefresh() {
		AddItemToContainer(++currentPage, 1);

	}

	@Override
	public void onLoadMore() {
		AddItemToContainer(++currentPage, 2);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.img_create:
			pop = new SelectPicPopwindow(this, v, "拍照", "从相册选择");
			pop.setmSelectOnclick(this);
			pop.show();
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
		Intent intent = new Intent();
		intent.putExtra("count", 0);
		intent.setClass(this, SelectPhotoActivity.class);
		startActivityForResult(intent,
				Constants.REQUEST_CODE_GETIMAGE_BYSDCARD_MORE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.REQUEST_CODE_GETIMAGE_BYSDCARD_MORE) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				@SuppressWarnings("unchecked")
				List<String> list = (List<String>) bundle
						.getSerializable("imgPath");
				List<PictureImage> imageList = new ArrayList<PictureImage>();
				for (String m : list) {
					PictureImage img = new PictureImage();
					img.setImage_url(m);
					imageList.add(img);
				}
				Picture picture = new Picture();
				picture.setContent("啊阿斯达克");
				picture.setImages(imageList);
				publishPic(picture);
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
			}
		});
		task.executeParallel(picture);
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
