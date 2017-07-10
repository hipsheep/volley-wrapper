package com.hipsheep.volleywrapper.network.request

import android.net.Uri
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.RequestFuture
import com.hipsheep.volleywrapper.VolleyWrapper
import com.hipsheep.volleywrapper.network.model.ResponseCallback
import org.json.JSONObject

/**
 * Base class for any request that is sent to the server.
 *
 * @param method
 * 		Request method to use (see [com.android.volley.Request.Method] to see the possible options).
 * @param url
 * 		URL of the request.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
open class BaseRequest<T>(method: Int, url: String) : Request<T>(method, url, null) {

    /**
     * Tag used to log errors, warnings, etc. on standard output, with the name of the class that extends [BaseRequest].
     */
    private val LOG_TAG: String = javaClass.name

    /**
     * Request URL, needed as a local variable to optimize the use of the [.getUrl()] method,
     * which is called several times during the Request life cycle.
     */
    private var _url: String? = null

    /**
     * Headers used on the request.
     */
    val headers = HashMap<String, String>()

    /**
     * Query parameters used on the request.
     */
    val queryParams = HashMap<String, String>()
    /**
     * Body parameters used on the request.
     */
    val bodyParams = HashMap<String, Any>()

    /**
     * Body of the request.
     * <p>
     * IMPORTANT: If this property is set, then any params in [bodyParams] will be ignored.
     */
    var body: String? = null

    /**
     * [ResponseCallback] that will be used to return the response for this request, on an asynchronous call.
     */
    var responseCallback: ResponseCallback<T>? = null
    /**
     * [RequestFuture] that will be used to return the response for this request, on a synchronous call.
     */
    var requestFuture: RequestFuture<T>? = null


    init {
        val defaultConfig = VolleyWrapper.defaultConfig

        // Set whether to cache responses or not (if it was set)
        val defaultShouldCache = defaultConfig?.shouldCache
        if (defaultShouldCache != null) {
            setShouldCache(defaultShouldCache)
        }

        // Set the default retry policy (if it was set)
        val defaultRetryPolicy = defaultConfig?.retryPolicy
        if (defaultRetryPolicy != null) {
            retryPolicy = defaultRetryPolicy
        }

        // Add the default headers (if any)
        val defaultHeaders = defaultConfig?.headers
        if (defaultHeaders != null) {
            headers.putAll(defaultHeaders)
        }
    }

    override fun getUrl(): String {
        if (_url != null) {
            // If the URL was generated once, return it from its saved variable, instead of regenerating it again
            return _url as String
        }

        var url = super.getUrl()

        if (!queryParams.isEmpty()) {
            val urlBuilder = Uri.parse(url).buildUpon()

            for ((key, value) in queryParams) {
                urlBuilder.appendQueryParameter(key, value)
            }

            url = urlBuilder.build().toString()
        }

        // Save URL so it doesn't need to be regenerated every time this method is called
        _url = url

        return url
    }

    /**
     * Adds a parameter to the request's query.
     *
     * @param key
     *      Key of the param to add to the request's query.
     * @param value
     *      Value of the param to add to the request's query.
     */
    protected fun addQueryParam(key: String, value: Any) {
        queryParams.put(key, value.toString())
    }

    override fun getBodyContentType(): String {
        return VolleyWrapper.defaultConfig?.bodyContentType ?: super.getBodyContentType()
    }

    override fun getHeaders(): MutableMap<String, String> {
        return headers
    }

    override fun getPostBody(): ByteArray? {
        return getBody()
    }

    override fun getBody(): ByteArray? {
        // If a body is not set, and parameters are set, then add the params into a JSON object
        // to send them on the request body
        if (body == null && !bodyParams.isEmpty()) {
            body = JSONObject(bodyParams).toString()
        }

        return body?.toByteArray()
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return createSuccessResponse(response, null)
    }

    open protected fun createSuccessResponse(response: NetworkResponse?, result: T?): Response<T> {
        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response))
    }

    override fun deliverResponse(response: T?) {
        // This is called for asynchronous calls
        responseCallback?.onSuccess(response)

        // This is called for synchronous calls
        requestFuture?.onResponse(response)
    }

    override fun deliverError(error: VolleyError?) {
        // This is called for asynchronous calls
        responseCallback?.onFailure(error)

        // This is called for synchronous calls
        requestFuture?.onErrorResponse(error)

        // Log error in the console for debugging purposes
        Log.w(LOG_TAG, "Request failed with error \"${error?.networkResponse?.statusCode ?: "null"}\", and no callback set for it")
    }

}