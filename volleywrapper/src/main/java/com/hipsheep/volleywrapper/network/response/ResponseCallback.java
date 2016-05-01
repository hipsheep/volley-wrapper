package com.hipsheep.volleywrapper.network.response;

import com.android.volley.VolleyError;

/**
 * Response callback for the network asynchronous calls.
 *
 * @param <T>
 * 		Expected type of successful response data.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
public abstract class ResponseCallback<T> {

	/**
	 * This callback will be called after a successful request.
	 *
	 * @param data
	 * 		Response data.
	 */
	public abstract void onSuccess(T data);

	/**
	 * This callback will be called after an unsuccessful request.
	 *
	 * @param volleyError
	 * 		Error with which the request failed.
	 */
	public abstract void onFailure(VolleyError volleyError);

}
