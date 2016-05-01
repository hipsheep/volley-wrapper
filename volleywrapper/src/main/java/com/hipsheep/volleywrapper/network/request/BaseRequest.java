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
import com.hipsheep.volleywrapper.network.response.ResponseCallback;

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
	 * Body content type to use for all requests (set through
	 * {@link com.hipsheep.volleywrapper.VolleyWrapper#setDefaultBodyContentType(String)}).
	 */
	private static String sDefaultBodyContentType;

	/**
	 * {@link RetryPolicy} to use for all requests (set through
	 * {@link com.hipsheep.volleywrapper.VolleyWrapper#setDefaultRetryPolicy(RetryPolicy)}).
	 */
	private static RetryPolicy sDefaultRetryPolicy;

	/**
	 * Flag that sets whether all requests should cache responses or not (set through
	 * {@link com.hipsheep.volleywrapper.VolleyWrapper#setDefaultShouldCache(Boolean)}).
	 */
	private static Boolean sDefaultShouldCache;

	/**
	 * Default headers to use for all requests (set through
	 * {@link com.hipsheep.volleywrapper.VolleyWrapper#setDefaultHeaders(Map)}).
	 */
	private static Map<String, String> sDefaultHeaders;

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

		// Set whether to cache responses or not, if the user set the value through VolleyWrapper
		if (sDefaultShouldCache != null) {
			setShouldCache(sDefaultShouldCache);
		}

		// Set the default retry policy, if the user set one through VolleyWrapper
		if (sDefaultRetryPolicy != null) {
			setRetryPolicy(sDefaultRetryPolicy);
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
		// We must set the content type here for POST requests, otherwise the server will fail to parse
		// the headers (because Volley uses a different content type value by default)
		return sDefaultBodyContentType != null ? sDefaultBodyContentType : super.getBodyContentType();
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return sDefaultHeaders != null ? sDefaultHeaders : super.getHeaders();
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

	/**
	 * Sets a retry policy to use as default for all requests.
	 *
	 * @param retryPolicy
	 * 		{@link RetryPolicy} to use for all requests.
	 */
	public static void setDefaultRetryPolicy(RetryPolicy retryPolicy) {
		sDefaultRetryPolicy = retryPolicy;
	}

	/**
	 * Sets whether all requests should cache responses or not.
	 *
	 * @param shouldCache
	 * 		{@code true} if all requests should cache responses, or {@code false} otherwise.
	 */
	public static void setDefaultShouldCache(Boolean shouldCache) {
		sDefaultShouldCache = shouldCache;
	}

	/**
	 * Sets a body content type to use as default for all requests.
	 *
	 * @param defaultBodyContentType
	 * 		Body content type to use for all requests.
	 */
	public static void setDefaultBodyContentType(String defaultBodyContentType) {
		sDefaultBodyContentType = defaultBodyContentType;
	}

	/**
	 * Sets the default headers to use for all requests.
	 *
	 * @param defaultHeaders
	 * 		Default headers to use for all requests.
	 */
	public static void setDefaultHeaders(Map<String, String> defaultHeaders) {
		sDefaultHeaders = defaultHeaders;
	}

}
