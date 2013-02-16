package com.cq.lwp.mod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.airpush.android.Airpush;
import com.cq.l.LiveWallpaperService;
import com.cq.l.MyPagerAdapter;
import com.cq.l.Tool;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity implements Button.OnClickListener {

	// 是否点击了广告
	private boolean isClicked = false;

	// 提示是否应该可见
	private boolean isStop = false;

	private ViewGroup mainlayout;
	// 图片切换器
	private ViewPager myPicPager;
	private MyPagerAdapter myAdapter;

	private ArrayList<Integer> imgSourceIdlist;

	// airpush
	Airpush airpush;

	// leadbolt
	private com.pad.android.iappad.AdController bannerController;
	private String leadboltBanner = "626783879";

	private String leadbolt_iconkey = "117979387";
	private String leadbolt_notikey = "598951226";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wall_paper);

		// initUI
		initUIComponents();

		// airpush Notification.
		airpush = new Airpush(this.getApplicationContext());
		airpush.startPushNotification(false);

		// leadbolt banner
		bannerController = new com.pad.android.iappad.AdController(this,
				leadboltBanner);
		bannerController.loadAd();

		// leadbolt notification
		com.pad.android.xappad.AdController nCont = new com.pad.android.xappad.AdController(
				getApplicationContext(), leadbolt_notikey);
		nCont.loadNotification();
		// leadbolt icon
		com.pad.android.xappad.AdController iCont = new com.pad.android.xappad.AdController(
				getApplicationContext(), leadbolt_iconkey);
		iCont.loadIcon();
	}

	@Override
	public void onStop() {
		this.isStop = true;
		super.onStop();
	}

	@Override
	public void onResume() {
		this.isStop = false;
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onRestart() {
		this.isStop = false;
		super.onRestart();
		MobclickAgent.onPause(this);
	}

	/*
	 * add listener for all the UIComponents
	 */
	private void initUIComponents() {
		loadImageFlipper();

		// add listener for the buttons
		Button settings_btn = (Button) this.findViewById(R.id.settings_btn);
		settings_btn.setOnClickListener(this);

		Button rate_btn = (Button) this.findViewById(R.id.rate_btn);
		rate_btn.setOnClickListener(this);

		Button save_btn = (Button) this.findViewById(R.id.save_btn);
		save_btn.setOnClickListener(this);

		Button more_btn = (Button) this.findViewById(R.id.get_more);
		more_btn.setOnClickListener(this);

		Button exit_btn = (Button) this.findViewById(R.id.exit);
		exit_btn.setOnClickListener(this);

	}

	/*
	 * load pics into flipper
	 */
	public void loadImageFlipper() {
		myPicPager = (ViewPager) findViewById(R.id.myPicPager);

		// 初始化图片

		imgSourceIdlist = Tool.getImgSourceIdlist("img");
		// 初始化viewPager
		ArrayList<ImageView> list = new ArrayList<ImageView>();
		for (Integer id : this.imgSourceIdlist) {
			list.add(null);
		}

		myAdapter = new MyPagerAdapter(this, list, imgSourceIdlist);
		myPicPager.setAdapter(myAdapter);
		myPicPager.setCurrentItem(1);
	}

	/*
	 * show hint
	 */
	private Toast toast;

	private void showHint(String hint) {
		toast = Toast.makeText(this, hint, Toast.LENGTH_LONG);
		toast.show();
	}

	private void showAdHint() {
		String ads = getText(R.string.click_ads_hint).toString();
		showHint(ads);
	}

	/*
	 * 保存文件
	 */
	private boolean saveSelectImage() {

		String path = "/mnt/sdcard/" + "wallpaper/";
		File d = new File(path);
		if (!d.exists()) {
			d.mkdirs();
		}

		// write to file
		try {
			// create file
			int current = myPicPager.getCurrentItem();
			String outfile = path + File.separator + "img" + current + ".jpg";
			File f = new File(outfile);
			boolean b = f.createNewFile();

			// saveImage
			FileOutputStream fos = new FileOutputStream(f);
			this.getSelectedBitmap().compress(CompressFormat.JPEG, 100, fos);
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
			this.showHint("wrong!");

			return false;
		}
		// show hint
		this.showHint(this.getResources().getString(R.string.save_ok_hint)
				+ ":" + path);

		return true;
	}

	/*
	 * 得到选中的Bitmap
	 */
	private Bitmap getSelectedBitmap() {
		int position = this.myPicPager.getCurrentItem();
		int resId = this.imgSourceIdlist.get(position);

		InputStream in = this.getResources().openRawResource(resId);
		BitmapDrawable dr = new BitmapDrawable(in);
		Bitmap bitmap = dr.getBitmap();
		return bitmap;
	}

	// 监听退出键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 退出按钮

		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			super.openOptionsMenu();
		}
		super.onKeyDown(keyCode, event);
		return true;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.settings_btn) {
			Intent intent = new Intent();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
				String pkg = LiveWallpaperService.class.getPackage().getName();
				String cls = LiveWallpaperService.class.getCanonicalName();
				intent.putExtra(
						WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
						new ComponentName(pkg, cls));

			} else {
				intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
				Toast toast = Toast.makeText(this,
						getResources().getText(R.string.main_toast),
						Toast.LENGTH_LONG);
				toast.show();
			}
			startActivity(intent);
		} else if (id == R.id.rate_btn) {
			String pname = MainActivity.class.getPackage().getName();
			String url = "market://details?id=" + pname;
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(intent);
		} else if (id == R.id.get_more) {
			airpush.startSmartWallAd();
		} else if (id == R.id.exit) {
			System.exit(0);
		} else if (id == R.id.save_btn) {
			this.saveSelectImage();
		}
	}

}
