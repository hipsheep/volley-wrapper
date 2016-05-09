package com.hipsheep.volleywrapper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.hipsheep.volleywrapper.network.request.BaseRequest;
import com.hipsheep.volleywrapper.network.response.ResponseCallback;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Main class for the library, used to send requests to the server.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
public class VolleyWrapper {

	private static RequestQueue sRequestQueue;


	/**
	 * Initializes the Volley Wrapper library, and creates the request queue that will be used for queueing the requests to send
	 * to the server.
	 *
	 * @param context
	 * 		{@link Context} used to create the request queue.
	 */
	public static void init(Context context) {
		sRequestQueue = Volley.newRequestQueue(context);
	}

	/**
	 * Initializes the Volley Wrapper library, and sets the request queue that will be used for queueing the requests to send
	 * to the server.
	 *
	 * @param requestQueue
	 * 		{@link RequestQueue} that will be used for queueing the requests to send to the server.
	 */
	public static void init(RequestQueue requestQueue) {
		sRequestQueue = requestQueue;
	}

	/**
	 * Sets a retry policy to use as default for all requests.
	 *
	 * @param retryPolicy
	 * 		{@link RetryPolicy} to use for all requests.
	 */
	public static void setDefaultRetryPolicy(RetryPolicy retryPolicy) {
		BaseRequest.setDefaultRetryPolicy(retryPolicy);
	}

	/**
	 * Sets whether all requests should cache responses or not.
	 *
	 * @param shouldCache
	 * 		{@code true} if all requests should cache responses, or {@code false} otherwise.
	 */
	public static void setDefaultShouldCache(Boolean shouldCache) {
		BaseRequest.setDefaultShouldCache(shouldCache);
	}

	/**
	 * Sets a body content type to use as default for all requests.
	 *
	 * @param defaultBodyContentType
	 * 		Body content type to use for all requests.
	 */
	public static void setDefaultBodyContentType(String defaultBodyContentType) {
		BaseRequest.setDefaultBodyContentType(defaultBodyContentType);
	}

	/**
	 * Sets the default headers to use for all requests.
	 *
	 * @param defaultHeaders
	 * 		Default headers to use for all requests.
	 */
	public static void setDefaultHeaders(Map<String, String> defaultHeaders) {
		BaseRequest.setDefaultHeaders(defaultHeaders);
	}

	/**
	 * Returns the {@link RequestQueue} used to send requests to the server.
	 *
	 * @return The {@link RequestQueue} used to send requests to the server.
	 */
	public static RequestQueue getRequestQueue() {
		return sRequestQueue;
	}

	/**
	 * Sends a request to the server asynchronously, and returns the response on the {@code responseCallback} received as parameter.
	 *
	 * @param request
	 * 		{@link Request} to send to the server.
	 * @param responseCallback
	 * 		{@link ResponseCallback} that will receive the response from this request.
	 */
	public static void sendRequest(BaseRequest request, ResponseCallback responseCallback) {
		// Set the callback before sending the request. We do this here so it doesn't have to be set on every request
		// constructor method
		request.setResponseCallback(responseCallback);

		sRequestQueue.add(request);
	}

	/**
	 * Sends a request to the server synchronously, and returns the response, or throws an {@link Exception} if an error occurred.
	 * <p>
	 * IMPORTANT: Don't call this method on the Main Thread or the app will freeze.
	 * <p>
	 * This method "returns" errors through the {@link Exception}s it throws. The {@link ExecutionException} is the only one of
	 * the 3 that will contain a {@link com.android.volley.VolleyError} inside, which you can get through the
	 * {@link ExecutionException#getCause()} method.
	 *
	 * @param request
	 * 		{@link Request} to send to the server.
	 *
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public static <T> T sendRequest(BaseRequest<T> request) throws ExecutionException, InterruptedException, TimeoutException {
		RequestFuture<T> requestFuture = RequestFuture.newFuture();

		request.setRequestFuture(requestFuture);
		sRequestQueue.add(request);

		// Time out if the response doesn't come back on 30 seconds
		return requestFuture.get(30, TimeUnit.SECONDS);
	}

}
