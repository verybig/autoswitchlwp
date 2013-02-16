package com.cq.l;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cq.lwp.mod.R;
import com.cq.lwp.mod.R.drawable;

public class Tool {

	private static ArrayList<Integer> imgSourceIdlist = null;

	/**
	 * 装载图片资源到数组
	 */
	public static ArrayList<Integer> getImgSourceIdlist(String prefix) {
		if (imgSourceIdlist != null) {
			return imgSourceIdlist;
		}
		// 用反射装载图片数组
		Class<drawable> cc = R.drawable.class;
		Field[] fields = cc.getFields();
		imgSourceIdlist = new ArrayList<Integer>();
		try {
			for (Field f : fields) {
				String s = f.getName();
				if (s.startsWith(prefix)) {
					int i = f.getInt(cc);
					imgSourceIdlist.add(i);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("cq", "load data error in ImageData.loadLocalImage");
		}
		return imgSourceIdlist;
	}

	/*
	 * set wallpaper
	 */
	public static boolean setWallPaper(Context context, Integer resourceId) {
		try {
			WallpaperManager wallpaperManager = WallpaperManager
					.getInstance(context);
			wallpaperManager.setResource(resourceId);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	// get an element_index from an arraylist
	public static int getRandomElement(ArrayList<Integer> intList) {
		int r = (int) Math.floor(intList.size() * Math.random());
		return r;
	}

	public static Bitmap getBitmap(Resources res, int imgId) {
		Bitmap bit = BitmapFactory.decodeResource(res, imgId);
		return bit;

	}

}
