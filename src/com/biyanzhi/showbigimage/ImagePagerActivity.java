package com.biyanzhi.showbigimage;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biyanzhi.R;
import com.biyanzhi.showbigimage.ImageDetailFragment.OnBack;
import com.biyanzhi.task.SaveImageTask;
import com.biyanzhi.task.SaveImageTask.SaveImge;
import com.biyanzhi.utils.Constants;
import com.biyanzhi.utils.ToastUtil;
import com.biyanzhi.view.HackyViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImagePagerActivity extends FragmentActivity implements OnBack,
		OnClickListener {
	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private List<String> lists = new ArrayList<String>();
	private ImagePagerAdapter mAdapter;

	private LinearLayout title;
	private ImageView img_back;
	private ImageView img_save;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_detail_pager);
		pagerPosition = getIntent().getIntExtra(Constants.EXTRA_IMAGE_INDEX, 0);
		lists = (List<String>) getIntent().getExtras().getSerializable(
				Constants.EXTRA_IMAGE_URLS);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), lists);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}
		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(Constants.STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
		title = (LinearLayout) findViewById(R.id.layTitle);
		initView();
	}

	private void initView() {
		img_back = (ImageView) findViewById(R.id.imgBack);
		img_save = (ImageView) findViewById(R.id.imgSave);
		setListener();
	}

	private void setListener() {
		img_back.setOnClickListener(this);
		img_save.setOnClickListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(Constants.STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public List<String> fileList;
		private FragmentManager fm;

		public ImagePagerAdapter(FragmentManager fm, List<String> fileList) {
			super(fm);
			this.fileList = fileList;
			this.fm = fm;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			fm.beginTransaction();

		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position);
			ImageDetailFragment fra = ImageDetailFragment.newInstance(url);
			fra.setCallBack(ImagePagerActivity.this);
			return fra;
		}
	}

	@Override
	public void onBackClick() {
		if (title.getVisibility() != View.VISIBLE) {
			title.setVisibility(View.VISIBLE);
			title.setAnimation(AnimationUtils.loadAnimation(this,
					R.anim.down_in));

		} else {
			title.setVisibility(View.INVISIBLE);
			title.setAnimation(AnimationUtils
					.loadAnimation(this, R.anim.up_out));

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBack:
			finish();
			break;
		case R.id.imgSave:
			img_save.setEnabled(false);
			if (lists.size() == 1) {
				pagerPosition = 0;
			}
			Bitmap bmp = ImageLoader.getInstance().loadImageSync(
					lists.get(pagerPosition));
			if (bmp != null) {
				SaveImageTask task = new SaveImageTask(bmp);
				task.setCallBack(new SaveImge() {
					@Override
					public void saveFinish() {
						img_save.setEnabled(true);
						ToastUtil.showToast("保存成功", Toast.LENGTH_SHORT);
					}
				});
				task.execute();
			} else {
				ToastUtil.showToast("保存失败", Toast.LENGTH_SHORT);
				img_save.setEnabled(true);

			}
			break;
		default:
			break;
		}
	}

}