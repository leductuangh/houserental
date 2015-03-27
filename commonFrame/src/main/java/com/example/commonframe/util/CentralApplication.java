package com.example.commonframe.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.commonframe.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class CentralApplication extends Application {
	private static Context mContext;
	private static Activity mActiveActivity;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		initImageLoader();
	}

	public static Context getContext() {
		return mContext;
	}

	public static void setActiveActivity(Activity active) {
		mActiveActivity = active;
	}

	public static Activity getActiveActivity() {
		return mActiveActivity;
	}

	@SuppressWarnings("deprecation")
	private void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(Constant.MEMORY_CACHE)
				.cacheOnDisk(Constant.DISC_CACHE).resetViewBeforeLoading(true)
				.showImageOnFail(R.drawable.broken)
				.showImageForEmptyUri(R.drawable.broken)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext).memoryCache(new WeakMemoryCache())
				.memoryCache(new LruMemoryCache(Constant.LRU_CACHE_SIZE))
				.memoryCacheSize(Constant.MEMORY_CACHE_SIZE)
				.discCacheSize(Constant.DISC_CACHE_SIZE)
				.discCacheFileCount(Constant.DISC_CACHE_COUNT)
				.denyCacheImageMultipleSizesInMemory().threadPoolSize(10)
				.threadPriority(Thread.MAX_PRIORITY)
				.defaultDisplayImageOptions(options).build();

		ImageLoader.getInstance().init(config);
	}
}
