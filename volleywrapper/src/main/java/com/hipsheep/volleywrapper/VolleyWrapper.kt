package com.hipsheep.volleywrapper

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.hipsheep.volleywrapper.network.model.ResponseCallback
import com.hipsheep.volleywrapper.network.request.BaseRequest
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Main class for the library, used to send requests to the server.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
object VolleyWrapper {

    /**
     * [RequestQueue] that will be used for queueing the requests to send to the server.
     */
    private var requestQueue: RequestQueue? = null

    /**
     * Default configuration to use for all requests.
     */
    var defaultConfig: Configuration? = null


    /**
     * Initializes the Volley Wrapper library, and creates the request queue that will be used for queueing the requests
     * to send to the server.
     *
     * @param context
     * 		[Context] used to create the request queue.
     */
    fun init(context: Context) {
        requestQueue = Volley.newRequestQueue(context)
    }

    /**
     * Initializes the Volley Wrapper library, and sets the request queue that will be used for queueing the requests
     * to send to the server.
     *
     * @param requestQueue
     *      [RequestQueue] that will be used for queueing the requests to send to the server.
     */
    fun init(requestQueue: RequestQueue) {
        this.requestQueue = requestQueue
    }

    /**
     * Sends a request to the server asynchronously, and returns the response on the `responseCallback` received as
     * parameter.
     *
     * @param request
     * 		[BaseRequest] to send to the server.
     * @param responseCallback
     * 		[ResponseCallback] that will receive the response from this request.
     */
    fun <T> sendRequest(request: BaseRequest<T>, responseCallback: ResponseCallback<T>?) {
        // Set the callback before sending the request. We do this here so it doesn't have to be set on every request
        // constructor method
        request.responseCallback = responseCallback

        requestQueue?.add(request)
    }

    /**
     * Sends a request to the server synchronously, and returns the response, or throws an [Exception] if an error
     * occurred.
     * <p>
     * IMPORTANT: Don't call this method on the Main Thread or the app will freeze.
     * <p>
     * This method "returns" errors through the [Exception]s it throws. The [ExecutionException] is the only one of
     * the 3 that will contain a [com.android.volley.VolleyError] inside, which you can get through the
     * [ExecutionException.cause] property.
     *
     * @param request
     * 		[BaseRequest] to send to the server.
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Throws(ExecutionException::class, InterruptedException::class, TimeoutException::class)
    fun <T> sendRequest(request: BaseRequest<T>): T {
        val requestFuture = RequestFuture.newFuture<T>()
        request.requestFuture = requestFuture

        requestQueue?.add(request)

        // If a sync timeout was set, then use it
        val syncTimeout = defaultConfig?.syncTimeout
        if (syncTimeout != null) {
            // Time out if the response doesn't come back before X seconds pass
            return requestFuture.get(syncTimeout, TimeUnit.SECONDS)
        } else {
            return requestFuture.get()
        }
    }

}