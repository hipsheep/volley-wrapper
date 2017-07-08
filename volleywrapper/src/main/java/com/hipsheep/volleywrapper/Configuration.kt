package com.hipsheep.volleywrapper

import com.android.volley.RetryPolicy

/**
 * Configuration that can be set to VolleyWrapper through the [VolleyWrapper.setDefaultConfiguration(Configuration)]
 * method.
 *
 * @property retryPolicy
 *      [RetryPolicy] to use for all requests.
 * @property shouldCache
 *      `true` if all requests should cache responses, or `false` otherwise.
 * @property bodyContentType
 *      Body content type to use for all requests.
 * @property headers
 *      Default headers to use for all requests.
 * @property syncTimeout
 *      Timeout value (in seconds) to use for sync requests. This is the value to use when calling the
 *      [RequestFuture#get(long, TimeUnit)] method, not to be confused with the timeout value that can be
 *      set using a [RetryPolicy] through the [retryPolicy] property.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
data class Configuration(val retryPolicy: RetryPolicy? = null,
                         val shouldCache: Boolean? = null,
                         val bodyContentType: String? = null,
                         val headers: Map<String, String>,
                         val syncTimeout: Long)