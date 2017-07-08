package com.hipsheep.volleywrapper.network.model

import com.android.volley.VolleyError

/**
 * Response callback for the network asynchronous calls.
 *
 * @param <T>
 * 		Expected type of successful response data.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
interface ResponseCallback<T> {

    /**
     * This callback will be called after a successful request.
     *
     * @param data
     * 		Response data.
     */
    fun onSuccess(data: T?)

    /**
     * This callback will be called after an unsuccessful request.
     *
     * @param volleyError
     * 		Error with which the request failed.
     */
    fun onFailure(volleyError: VolleyError?)

}