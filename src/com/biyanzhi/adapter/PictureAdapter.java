package com.biyanzhi.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biyanzhi.R;
import com.biyanzhi.activity.PictureCommentActivity;
import com.biyanzhi.data.Picture;
import com.biyanzhi.utils.UniversalImageLoadTool;
import com.biyanzhi.utils.Utils;
import com.biyanzhi.view.RoundAnglePictureImageView;

public class PictureAdapter extends BaseAdapter {
	private List<Picture> mLists;
	private Context mContext;
	private int width;

	public PictureAdapter(Context context, List<Picture> mLists) {
		this.mLists = mLists;
		this.mContext = context;
		width = Utils.getSecreenWidth(context) / 2 - 18;// 24 margin值
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		Picture picture = mLists.get(position);
		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(mContext);
			convertView = layoutInflator.inflate(R.layout.grid_view_item_1,
					null);
			holder = new ViewHolder();
			holder.imageView = (RoundAnglePictureImageView) convertView
					.findViewById(R.id.img);
			holder.contentView = (TextView) convertView
					.findViewById(R.id.txt_content);
			holder.parent_layout = (LinearLayout) convertView
					.findViewById(R.id.parent);
			holder.txt_score = (TextView) convertView
					.findViewById(R.id.txt_score);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_score.setText(Html.fromHtml("<font color=#F06617>"
				+ picture.getScore_number() + "人</font>  参与评分 平均颜值"
				+ "<font color=#F06617>(" + picture.getAverage_score()
				+ "分)</font>"));
		holder.contentView.setText(picture.getContent());
		LayoutParams layoutParams = holder.imageView.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = width;
		holder.imageView.setLayoutParams(layoutParams);
		String path = picture.getPicture_image_url();
		if (!path.startsWith("http")) {
			path = "file://" + path;
		}
		UniversalImageLoadTool.disPlay(path, holder.imageView,
				R.drawable.empty_photo);
		holder.imageView.setOnClickListener(new OnClick(position));
		return convertView;
	}

	@Override
	public int getCount() {
		return mLists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mLists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	class ViewHolder {
		LinearLayout parent_layout;
		RoundAnglePictureImageView imageView;
		TextView contentView;
		TextView timeView;
		TextView txt_score;
	}

	class OnClick implements OnClickListener {
		private int position;

		public OnClick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			mContext.startActivity(new Intent(mContext,
					PictureCommentActivity.class).putExtra("picture",
					mLists.get(position)));
			Utils.leftOutRightIn(mContext);
		}

	}
}