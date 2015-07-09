package com.avai.amp.prx.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.net.http.AndroidHttpClient;
import android.util.Log;

public class HttpAmpService {

	public enum HttpMethod {
		POST, GET
	}

	private String url;
	private boolean success = false;
	private boolean useGzip = false;
	private boolean setTimeout = false;
	private String contentType = "application/json";
	// TODO
	public static final String JSON_CONTENT = "json_content";
	private static final String TAG = "HttpAmpService";
	private static final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

	private AndroidHttpClient myHttpClient;
	private HttpResponse response = null;

	public HttpAmpService(String url) {
		// TODO Auto-generated constructor stub
		this.url = url;
		myHttpClient = AndroidHttpClient.newInstance("unknown");// unknown user agent?
	}

	public HttpAmpService(String url, String contentType, boolean useGzip, boolean setTimeout) {
		this(url);
		this.contentType = contentType;
		this.useGzip = useGzip;
		this.setTimeout = setTimeout;
	}

	/**
	 * @param body JSON formatted to String
	 * @param contentType
	 * @param methodType
	 * @param setTimeout
	 * @param useGzip
	 */
	public void sendRequestOnly(String body, String contentType, int methodType) {
		switch (methodType) {
		case 0:
			sendPostRequest(body, contentType);
			break;
		case 1:
			sendGetRequest(contentType);
			break;
		default:
			Log.e(TAG, "method type on sendRequestOnly should be 0 or 1");
		}
	}

	private void sendPostRequest(String body, String contentType) {
		Log.i(TAG, "HTTP Posting " + body + " to " + url);

		HttpPost httpPostRequest = new HttpPost(url);
		try {
			// Set HTTP parameters

			addRequestHeaders(httpPostRequest, contentType);
			if (useGzip) {
				httpPostRequest.addHeader("Accept-Encoding", "gzip");
				httpPostRequest.setEntity(AndroidHttpClient.getCompressedEntity(body.getBytes(), null));
				// TODO
			} else {
				httpPostRequest.setEntity(new StringEntity(body));
			}
			// TODO uncompress G-zip
			long t = System.currentTimeMillis();
			response = myHttpClient.execute(httpPostRequest);
			Log.i(TAG, "HTTP Post response received in [" + (System.currentTimeMillis() - t) + "ms]");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO handle http io exception
			e.printStackTrace();
		}
	}

	private void sendGetRequest(String contentType) {
		Log.i(TAG, "HTTP Getting from URL " + url);

		HttpGet httpGetRequest = new HttpGet(url);

		try {
			// Set HTTP parameters
			addRequestHeaders(httpGetRequest, contentType);
			if (useGzip)
				httpGetRequest.addHeader("Accept-Encoding", "gzip");
			// TODO uncompress G-zip
			long t = System.currentTimeMillis();
			response = myHttpClient.execute(httpGetRequest);
			Log.i(TAG, "HTTP Get response received in [" + (System.currentTimeMillis() - t) + "ms]");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO handle http io exception
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getStatusCode() {
		if (response != null)
			return response.getStatusLine().getStatusCode();
		return 0;
	}

	public String getReasonPhrase() {
		if (response != null)
			return response.getStatusLine().getReasonPhrase();
		return null;
	}

	public void addRequestHeaders(HttpUriRequest request, String contentType) {
		request.addHeader("Accept", contentType);
		request.addHeader("Content-type", contentType);
	}

	public <T> T getJsonForGet(Class<T> returnType) {
		sendRequestOnly(null, contentType, 1);

		T ret = null;
		if (response != null) {
			try {
				ret = gson.fromJson(EntityUtils.toString(response.getEntity()), returnType);
			} catch (ParseException e) {
				// TODO
				e.printStackTrace();
			} catch (IOException e) {
				// TODO
				e.printStackTrace();
			}
		}

		return ret;
	}

	public void getJsonForPost(Object inputTO) {
		sendRequestOnly(gson.toJson(inputTO), contentType, 0);
		// TODO
	}

	public <T> T getJsonForPost(Object inputTO, Class<T> returnType) {
		getJsonForPost(inputTO);
		success = false;

		T ret = null;
		if (response != null) {
			try {
				ret = gson.fromJson(EntityUtils.toString(response.getEntity()), returnType);
				if (getStatusCode() == HttpStatus.SC_OK || getStatusCode() == HttpStatus.SC_NO_CONTENT)
					success = true;
				// TODO
			} catch (ParseException e) {
				// TODO
				e.printStackTrace();
			} catch (IOException e) {
				// TODO
				e.printStackTrace();
			}
		}

		return ret;
	}

	public boolean isSuccess() {
		return (getStatusCode() == HttpStatus.SC_OK || getStatusCode() == HttpStatus.SC_NO_CONTENT);
	}
}
