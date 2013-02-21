package com.cq.l;

//addadd testtest
import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.cq.lwp.hellok.MainActivity;

public class MyPagerAdapter extends PagerAdapter {
	private ArrayList<ImageView> views;
	private ArrayList<Integer> images;
	MainActivity parent;

	public MyPagerAdapter(MainActivity parent, ArrayList<ImageView> views,
			ArrayList<Integer> images) {
		this.views = views;
		this.images = images;
		this.parent = parent;
	}

	@Override
	public void destroyItem(View collection, int position, Object itemview) {
		// Log.d("OOM", "destroyItem " + position + " itemView " + itemview);

		ImageView v = views.get(position);
		((ViewPager) collection).removeView(v);
		views.set(position, null);
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		// Log.d("OOM", "instantiate " + position);
		ImageView iv = null;
		try {
			if (views.get(position) == null) {
				int imageId = images.get(position);
				iv = new ImageView(parent);
				iv.setImageResource(imageId);
				views.set(position, iv);
			} else {
				iv = views.get(position);
			}
			((ViewPager) collection).addView(iv, 0);
		} catch (Exception e) {
		}
		return iv;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == (object);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

}