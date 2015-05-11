package com.biyanzhi.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biyanzhi.R;
import com.biyanzhi.chooseimage.SelectPhotoActivity;
import com.biyanzhi.popwindow.SelectPicPopwindow;
import com.biyanzhi.popwindow.SelectPicPopwindow.SelectOnclick;
import com.biyanzhi.utils.Constants;
import com.biyanzhi.utils.FileUtils;
import com.biyanzhi.utils.UniversalImageLoadTool;
import com.dodola.model.DuitangInfo;
import com.dodowaterfall.Helper;
import com.dodowaterfall.widget.ScaleImageView;
import com.example.android.bitmapfun.util.ImageFetcher;

public class MainActivity extends BaseActivity implements IXListViewListener,
		SelectOnclick {
	// private ImageFetcher mImageFetcher;
	private XListView mAdapterView = null;
	private StaggeredAdapter mAdapter = null;
	private int currentPage = 0;
	private SelectPicPopwindow pop;
	private ImageView img_select;
	private String cameraPath = "";

	/**
	 * 添加内容
	 * 
	 * @param pageindex
	 * @param type
	 *            1为下拉刷新 2为加载更多
	 */
	private void AddItemToContainer(int pageindex, int type) {

	}

	public class StaggeredAdapter extends BaseAdapter {
		private LinkedList<DuitangInfo> mInfos;

		public StaggeredAdapter(Context context, XListView xListView) {
			mInfos = new LinkedList<DuitangInfo>();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			DuitangInfo duitangInfo = mInfos.get(position);

			if (convertView == null) {
				LayoutInflater layoutInflator = LayoutInflater.from(parent
						.getContext());
				convertView = layoutInflator.inflate(R.layout.infos_list, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.news_pic);
				holder.contentView = (TextView) convertView
						.findViewById(R.id.news_title);
				convertView.setTag(holder);
			}

			holder = (ViewHolder) convertView.getTag();
			// holder.imageView.setImageWidth(duitangInfo.getWidth());
			// holder.imageView.setImageHeight(duitangInfo.getHeight());
			System.out.println("hight::::::::::;" + duitangInfo.getHeight());
			holder.contentView.setText(duitangInfo.getMsg());
			// mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.imageView);
			String path = duitangInfo.getIsrc();
			if (!path.startsWith("http")) {
				path = "file://" + path;
			}
			UniversalImageLoadTool.disPlay(path, holder.imageView,
					R.drawable.empty_photo);
			return convertView;
		}

		class ViewHolder {
			ImageView imageView;
			TextView contentView;
			TextView timeView;
		}

		@Override
		public int getCount() {
			return mInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mInfos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		public void addItemLast(List<DuitangInfo> datas) {
			mInfos.addAll(datas);
		}

		public void addItemTop(List<DuitangInfo> datas) {
			for (DuitangInfo info : datas) {
				mInfos.addFirst(info);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAdapterView = (XListView) findViewById(R.id.list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		mAdapter = new StaggeredAdapter(this, mAdapterView);
		mAdapterView.setAdapter(mAdapter);
		initView();
	}

	private void initView() {
		img_select = (ImageView) findViewById(R.id.img_create);
		img_select.setOnClickListener(this);
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
				List<DuitangInfo> result = new ArrayList<DuitangInfo>();
				for (String m : list) {
					DuitangInfo info = new DuitangInfo();
					info.setHeight(300);
					info.setIsrc(m);
					info.setMsg("aaaaaaaaaaaaa");
					result.add(info);
				}
				mAdapter.addItemLast(result);
				mAdapter.notifyDataSetChanged();
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
