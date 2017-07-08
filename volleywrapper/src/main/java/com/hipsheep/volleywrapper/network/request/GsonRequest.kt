package com.hipsheep.volleywrapper.network.request

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/**
 * Request that receives a JSON response, and uses the Gson library to parse it to an object.
 *
 * @param method
 * 		Request method to use (see [com.android.volley.Request.Method] to see the possible options).
 * @param url
 * 		URL of the request.
 * @param responseClass
 * 		Class type of the object that will be returned as a response to the request.
 * @param gson
 * 		[Gson] object that will be used to parse the response.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
open class GsonRequest<T>(method: Int,
                          url: String,
                          private val responseClass: Class<T>,
                          private val gson: Gson) : BaseRequest<T>(method, url) {

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        try {
            val json = response?.data?.toString(Charset.forName(HttpHeaderParser.parseCharset(response.headers)))

            return createSuccessResponse(response, gson.fromJson(json, responseClass))
        } catch (e: UnsupportedEncodingException) {
            return Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            return Response.error(ParseError(e))
        }
    }

}