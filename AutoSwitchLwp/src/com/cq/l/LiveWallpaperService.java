package com.cq.l;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {
	private final Handler drawHandler = new Handler();

	@Override
	public Engine onCreateEngine() {
		return new LiveEngine();
	}

	public class LiveEngine extends Engine {

		private int delayTime = 10000;
		private boolean visible;

		Canvas c = null;
		Paint paint = null;

		private final Runnable drawRannable = new Runnable() {
			public void run() {
				draw();
			}
		};

		public LiveEngine() {
			SharedPreferences setting = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());

			paint = new Paint();

		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			drawHandler.removeCallbacks(drawRannable);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			this.visible = visible;
			if (visible) {
				draw();
			} else {
				drawHandler.removeCallbacks(drawRannable);
			}
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
			super.onSurfaceCreated(surfaceHolder);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);

		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			visible = false;
			drawHandler.removeCallbacks(drawRannable);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {

		}

		private void draw() {
			final SurfaceHolder holder = getSurfaceHolder();

			try {
				c = holder.lockCanvas();
				if (c != null) {
					// draw a bitmap
					ArrayList<Integer> al = Tool.getImgSourceIdlist("img");
					int index = Tool.getRandomElement(al);
					Bitmap bt = Tool.getBitmap(getResources(), al.get(index));

					Bitmap scbt = bt.createScaledBitmap(bt, c.getWidth(),
							c.getHeight(), true);

					c.drawBitmap(scbt, 0, 0, paint);
					scbt = null;
					bt = null;

				}
			} finally {
				if (c != null) {
					holder.unlockCanvasAndPost(c);
				}
			}

			drawHandler.removeCallbacks(drawRannable);
			if (visible) {
				drawHandler.postDelayed(drawRannable, delayTime);
			}
		}

	}
}