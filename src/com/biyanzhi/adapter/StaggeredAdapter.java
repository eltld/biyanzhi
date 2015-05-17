package com.biyanzhi.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biyanzhi.R;
import com.biyanzhi.activity.PictureCommentActivity;
import com.biyanzhi.data.Picture;
import com.biyanzhi.utils.UniversalImageLoadTool;
import com.biyanzhi.utils.Utils;

public class StaggeredAdapter extends BaseAdapter {
	private List<Picture> mLists;
	private Context mContext;

	public StaggeredAdapter(Context context, List<Picture> mLists) {
		this.mLists = mLists;
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		Picture picture = mLists.get(position);
		if (convertView == null) {
			LayoutInflater layoutInflator = LayoutInflater.from(mContext);
			convertView = layoutInflator.inflate(R.layout.infos_list, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.news_pic);
			holder.contentView = (TextView) convertView
					.findViewById(R.id.news_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String content = picture.getContent();
		if ("".equals(content)) {
			holder.contentView.setVisibility(View.GONE);
		} else {
			holder.contentView.setVisibility(View.VISIBLE);
			holder.contentView.setText(picture.getContent());
		}
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
		ImageView imageView;
		TextView contentView;
		TextView timeView;
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