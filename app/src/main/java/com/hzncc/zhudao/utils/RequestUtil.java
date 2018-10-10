package com.hzncc.zhudao.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hzncc.zhudao.R;

public class RequestUtil implements ImageCache {
	public static final String CONNECT_FAILED = "网络链接失败，请检查网络链接！";
	public static final String SERVER_PROBLEM = "找不到服务器！";
	public static final String CONTENT_IS_NULL = "获取的数据为空！";
	public static final String CONNECT_WEB_EXCEPTION = "网络不稳定，请稍后再试！";
	public static final String CONNECT_WEB_TIME_OUT = "链接超时，请稍后再试！";

	public static RequestQueue mQueue;
	private Reply reply;
	private ImageLoader imageLoader;
	public static String PHPSESSID = null;

	private LruCache<String, Bitmap> mCache;

	public RequestUtil(Context context) {
		initQueue(context);
		imageLoader = new ImageLoader(mQueue, this);
		int maxSize = 2 * 1024 * 1024;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	public RequestUtil(Context context, Reply reply) {
		initQueue(context);
		setReply(reply);
		imageLoader = new ImageLoader(mQueue, this);
		int maxSize = 2 * 1024 * 1024;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	public void initQueue(Context context) {
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(context.getApplicationContext());
			File cacheDir = new File(context.getCacheDir(), "volley");
			DiskBasedCache cache = new DiskBasedCache(cacheDir);
			mQueue.start();

			// clear all volley caches.
			mQueue.add(new ClearCacheRequest(cache, null));
		}
	}

	private RequestQueue getmQueue() {
		return mQueue;
	}

	public Reply getReply() {
		return reply;
	}

	public void setReply(Reply reply) {
		this.reply = reply;
	}

	public void getRequest(String url, final int what) {
		StringRequest stringRequest = new StringRequest(Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						onGetSuccess(response, what);
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						onError(error, what);
					}
				});
		getmQueue().add(stringRequest);
	}

	public void postRequest(String url, final int what) {
		postRequest(url, null, what, true);
	}

	public void postRequest(String url, final int what, boolean isCache) {
		postRequest(url, null, what, isCache);
	}

	public void postRequest(String url, final Map<String, Object> map,
			final int what) {
		postRequest(url, map, what, true);
	}

	public void postRequest(String url, final Map<String, Object> map,
			final int what, boolean isCache) {
		JSONObject jsonObject = null;
		if (null == map) {
			jsonObject = new JSONObject();
		} else {
			jsonObject = new JSONObject(map);
		}
		JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
				Method.POST, url, jsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						onPostSuccess(response, what);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						onError(error, what);
					}
				}) {
			// @Override
			// protected Response<JSONObject> parseNetworkResponse(
			// NetworkResponse response) {
			// return getCookie(response);
			// }

			// @Override
			// public Map<String, String> getHeaders() {
			// return setHeaders();
			// }
		};
		jsonRequest.setShouldCache(isCache);
		getmQueue().add(jsonRequest);
	}

	public void onGetSuccess(String response, int what) {
		// if (null == response || "".equals(response) ||
		// "null".equals(response)) {
		// if (null != reply) {
		// reply.onErrorResponse(CONTENT_IS_NULL, what);
		// }
		// return;
		// } else {
		reply.onGetSuccess(response, what);
		// }
	}

	public void onPostSuccess(JSONObject response, int what) {
		// if (null == response || "".equals(response) ||
		// "null".equals(response)) {
		// if (null != reply) {
		// reply.onErrorResponse(CONTENT_IS_NULL, what);
		// }
		// return;
		// } else {
		reply.onPostSuccess(response, what);
		// }
	}

	public void onError(VolleyError error, int what) {
		if (null != reply) {
			reply.onErrorResponse(getMessage(error), what);
		}
	}

	public static String getMessage(Object error) {
		if (error instanceof TimeoutError) {
			return CONNECT_WEB_TIME_OUT;
		} else if (isServerProblem(error)) {
			return SERVER_PROBLEM;
		} else if (isNetworkProblem(error)) {
			return CONNECT_FAILED;
		} else
			return CONTENT_IS_NULL;
	}

	private static boolean isNetworkProblem(Object error) {
		return (error instanceof NetworkError)
				|| (error instanceof NoConnectionError);
	}

	private static boolean isServerProblem(Object error) {
		return (error instanceof ServerError)
				|| (error instanceof AuthFailureError);
	}

	//
	// public Response<JSONObject> getCookie(NetworkResponse response) {
	// try {
	// Map<String, String> responseHeaders = response.headers;
	// String string = responseHeaders.get("Set-Cookie");
	// if (string != null) {
	// if (string.contains("PHPSESSID")) {
	// PHPSESSID = string.split(";")[0];
	// }
	// }
	// String jsonString = new String(response.data,
	// HttpHeaderParser.parseCharset(response.headers));
	// JSONObject jsonObject = new JSONObject(jsonString);
	// return Response.success(jsonObject,
	// HttpHeaderParser.parseCacheHeaders(response));
	// } catch (UnsupportedEncodingException e) {
	// return Response.error(new ParseError(e));
	// } catch (JSONException je) {
	// return Response.error(new ParseError(je));
	// }
	// }

	public Map<String, String> setHeaders() {
		HashMap<String, String> headers = new HashMap<String, String>();
		if (null != PHPSESSID && !"".equals(PHPSESSID)) {
			headers.put("Cookie", PHPSESSID);
		}
		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json; charset=UTF-8");
		return headers;
	}

	public void getImage(String url, Response.Listener<Bitmap> listener,
			Response.ErrorListener errorListener) {
		ImageRequest imageRequest = new ImageRequest(url, listener, 0, 0,
				Config.RGB_565, errorListener);
		getmQueue().add(imageRequest);
	}

	public void getImage(ImageView imageView, String url) {
		ImageListener listener = ImageLoader.getImageListener(imageView,
				R.mipmap.loading_big, R.mipmap.fail_big);
		imageLoader.get(url, listener);
	}

	public void get5p4Image(ImageView imageView, String url) {
		getImage(imageView, url, 150, 120);
	}

	public void getHeadImage(ImageView imageView, String url) {
		getImage(imageView, url, 300, 300);
	}

	public void getBigHeadImage(ImageView imageView, String url) {
		getImage(imageView, url, 800, 800);
	}

	public void getMiniHeadImage(ImageView imageView, String url) {
		getImage(imageView, url, 100, 100);
	}

	public void getItemImage(ImageView imageView, String url) {
		getImage(imageView, url, 400, 400);
	}

	public void getBigItemImage(ImageView imageView, String url) {
		getImage(imageView, url, 800, 800);
	}

	public void getImage(ImageView imageView, String url, int width, int height) {
		ImageListener listener = ImageLoader.getImageListener(imageView,
				R.mipmap.loading, R.mipmap.fail);
		imageLoader.get(url, listener, width, height);
	}

	public void getImage(String url, ImageListener listener) {
		imageLoader.get(url, listener);
	}

	public void getLocalImage(final String path, final ImageCallback callback) {
		getLocalImage(path, 0, 0, callback);
	}

	public void getLocalImage(final String path, final Options options,
			final ImageCallback callback) {
		final String key = getCacheKey(path, 0, 0, options.inSampleSize);
		Bitmap bitmap = getBitmap(key);
		if (null != bitmap) {
			callback.imageLoaded(bitmap);
		}
		final Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				callback.imageLoaded((Bitmap) msg.obj);
			};
		};
		new Thread() {
			@Override
			public void run() {
				Bitmap bm = BitmapFactory.decodeFile(path, options);
				putBitmap(key, bm);
				Message msg = handler.obtainMessage(0, bm);
				handler.sendMessage(msg);
			}
		}.start();
	}

	public void getLocalImage(final String path, final int width,
			final int height, final ImageCallback callback) {
		final String key = getCacheKey(path, width, height, 0);
		Bitmap bitmap = getBitmap(key);
		if (null != bitmap) {
			callback.imageLoaded(bitmap);
		}
		final Handler handler = new Handler() {

			public void handleMessage(Message msg) {
				callback.imageLoaded((Bitmap) msg.obj);
			};
		};
		new Thread() {
			@Override
			public void run() {
				Bitmap bm = Bitmap.createScaledBitmap(
						BitmapFactory.decodeFile(path), width, height, true);
				putBitmap(key, bm);
				Message msg = handler.obtainMessage(0, bm);
				handler.sendMessage(msg);
			}
		}.start();
	}

	public interface ImageCallback {
		public void imageLoaded(Bitmap bitmap);
	}

	private int getSdCardFreeSpace() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double freespace = stat.getAvailableBlocks() * stat.getBlockSize();
		return (int) (freespace / 1024 / 1024);
	}

	private boolean isSdCardAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	@Override
	public Bitmap getBitmap(String url) {
		return mCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if(null==url||null==bitmap){
			return;
		}
		mCache.put(url, bitmap);
	}

	/**
	 * 将输入流转成字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8 * 1024);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "n");
			}
		} catch (IOException e) {
			sb.delete(0, sb.length());
		} finally {
			try {
				is.close();
			} catch (IOException e) {

			}
		}

		return sb.toString();
	}

	public interface Reply {
		public abstract void onPostSuccess(JSONObject response, int what);

		public abstract void onGetSuccess(String response, int what);

		public abstract void onErrorResponse(String error, int what);

	}

	/**
	 * Creates a cache key for use with the L1 cache.
	 * 
	 * @param url
	 *            The URL of the request.
	 * @param maxWidth
	 *            The max-width of the output.
	 * @param maxHeight
	 *            The max-height of the output.
	 */
	private static String getCacheKey(String url, int maxWidth, int maxHeight,
			int options) {
		return new StringBuilder(url.length() + 12).append("#W")
				.append(maxWidth).append("#H").append(maxHeight).append("#O")
				.append(options).append(url).toString();
	}
}
