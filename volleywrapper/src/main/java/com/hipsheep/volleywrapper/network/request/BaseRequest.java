package com.hipsheep.volleywrapper.network.request;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.RequestFuture;
import com.hipsheep.volleywrapper.Configuration;
import com.hipsheep.volleywrapper.VolleyWrapper;
import com.hipsheep.volleywrapper.network.model.ResponseCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for any request that is sent to the server.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
public abstract class BaseRequest<T> extends Request<T> {

	/**
	 * Tag used to log errors, warnings, etc. on standard output, with the name of the class that extends {@link BaseRequest}.
	 */
	private final String mLogTag = getClass().getName();

	/**
	 * Request URL, needed as a local variable to optimize the use of the {@link #getUrl()} method,
	 * which is called several times during the Request life cycle.
	 */
	private String mUrl;

	/**
	 * Headers used on the request.
	 */
	private Map<String, String> mHeaders = new HashMap<>();

	/**
	 * Query parameters used on the request.
	 */
	private Map<String, String> mQueryParams = new HashMap<>();
	/**
	 * Body parameters used on the request.
	 */
	private Map<String, Object> mBodyParams = new HashMap<>();

	/**
	 * Body of the request.
	 */
	private String mBody;

	/**
	 * Callback to use for asynchronous calls.
	 */
	private ResponseCallback<T> mResponseCallback;
	/**
	 * Callback to use for synchronous calls.
	 */
	private RequestFuture<T> mRequestFuture;


	/**
	 * Constructor method for requests that return JSON responses.
	 *
	 * @param method
	 * 		Request method to use (see {@link com.android.volley.Request.Method} to see the possible options).
	 * @param url
	 * 		URL of the request.
	 */
	protected BaseRequest(int method, String url) {
		super(method, url, null);

		Configuration defaultConfiguration = VolleyWrapper.getDefaultConfiguration();

		// Set whether to cache responses or not (if it was set)
		Boolean defaultIsShouldCache = defaultConfiguration.isShouldCache();
		if (defaultIsShouldCache != null) {
			setShouldCache(defaultIsShouldCache);
		}

		// Set the default retry policy (if it was set)
		RetryPolicy defaultRetryPolicy = defaultConfiguration.getRetryPolicy();
		if (defaultRetryPolicy != null) {
			setRetryPolicy(defaultRetryPolicy);
		}

		// Add the default headers (if any)
		Map<String, String> defaultHeaders = defaultConfiguration.getHeaders();
		if (defaultHeaders != null) {
			mHeaders.putAll(defaultHeaders);
		}
	}

	@Override
	public String getUrl() {
		if (mUrl != null) {
			// If the URL was generated once, return it from its saved variable, instead of regenerating it again
			return mUrl;
		}

		String url = super.getUrl();

		if (!mQueryParams.isEmpty()) {
			Uri.Builder urlBuilder = Uri.parse(url).buildUpon();

			for (Map.Entry<String, String> param : mQueryParams.entrySet()) {
				urlBuilder.appendQueryParameter(param.getKey(), param.getValue());
			}

			url = urlBuilder.build().toString();
		}

		// Save URL so it doesn't need to be regenerated every time this method is called
		mUrl = url;

		return url;
	}

	@Override
	public String getBodyContentType() {
		String defaultBodyContentType = VolleyWrapper.getDefaultConfiguration().getBodyContentType();
		return defaultBodyContentType != null ? defaultBodyContentType : super.getBodyContentType();
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return mHeaders;
	}

	/**
	 * Adds a header to the request, but only if the header's value is not {@code null}.
	 *
	 * @param key
	 * 		Key of the header to add to the request.
	 * @param value
	 * 		Value of the header to add to the request. If this value is {@code null} the header won't be added.
	 */
	protected void addHeader(String key, String value) {
		if (value != null) {
			mHeaders.put(key, value);
		}
	}

	/**
	 * Adds a parameter to the request's query, but only if the param's value is not {@code null}.
	 *
	 * @param key
	 * 		Key of the param to add to the request's query.
	 * @param value
	 * 		Value of the param to add to the request's query. If this value is {@code null} the param won't be added.
	 */
	protected void addQueryParam(String key, Object value) {
		if (value != null) {
			mQueryParams.put(key, value.toString());
		}
	}

	/**
	 * Adds a parameter to the request's body.
	 *
	 * @param key
	 * 		Key of the param to add to the request's body.
	 * @param value
	 * 		Value of the param to add to the request's body.
	 */
	protected void addBodyParam(String key, Object value) {
		mBodyParams.put(key, value);
	}

	/**
	 * Sets the body of the request. If this method is called, then any params set using {@link #addBodyParam(String, Object)}
	 * will be ignored.
	 *
	 * @param requestBody
	 * 		Body of the request.
	 */
	protected void setBody(String requestBody) {
		mBody = requestBody;
	}

	@Override
	public byte[] getPostBody() {
		return getBody();
	}

	@Override
	public byte[] getBody() {
		// If a body is not set, and parameters are set, then add the params into a JSON object
		// to send them on the request body
		if (mBody == null && !mBodyParams.isEmpty()) {
			mBody = (new JSONObject(mBodyParams)).toString();
		}

		return mBody == null ? null : mBody.getBytes();
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		return createSuccessResponse(response, null);
	}

	protected Response<T> createSuccessResponse(NetworkResponse response, T result) {
		return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(T response) {
		if (mResponseCallback != null) {
			// This is called for asynchronous calls
			mResponseCallback.onSuccess(response);
		} else if (mRequestFuture != null) {
			// This is called for synchronous calls
			mRequestFuture.onResponse(response);
		}
	}

	@Override
	public void deliverError(VolleyError volleyError) {
		if (mResponseCallback != null) {
			// This is called for asynchronous calls
			mResponseCallback.onFailure(volleyError);
		} else if (mRequestFuture != null) {
			// This is called for synchronous calls
			mRequestFuture.onErrorResponse(volleyError);
		} else {
			NetworkResponse networkResponse = volleyError.networkResponse;
			Log.w(mLogTag, String.format("Request failed with error \"%s\", and no callback set for it",
					networkResponse == null ? "null" : networkResponse.statusCode));
		}
	}

	/**
	 * Sets the {@link ResponseCallback} that will be used to return the response for this request, on an asynchronous call.
	 *
	 * @param responseCallback
	 * 		{@link ResponseCallback} that will be used to return the response for this request, on an asynchronous call.
	 */
	public void setResponseCallback(ResponseCallback<T> responseCallback) {
		mResponseCallback = responseCallback;
	}

	/**
	 * Sets the {@link RequestFuture} that will be used to return the response for this request, on a synchronous call.
	 *
	 * @param requestFuture
	 * 		{@link RequestFuture} that will be used to return the response for this request, on a synchronous call.
	 */
	public void setRequestFuture(RequestFuture<T> requestFuture) {
		mRequestFuture = requestFuture;
	}

}
