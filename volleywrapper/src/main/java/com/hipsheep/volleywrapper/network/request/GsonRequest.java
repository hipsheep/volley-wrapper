package com.hipsheep.volleywrapper.network.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hipsheep.volleywrapper.util.GsonUtils;

import java.io.UnsupportedEncodingException;

/**
 * Request that receives a JSON response, and uses the Gson library to parse it to an object.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
public class GsonRequest<T> extends BaseRequest<T> {

	private final Class<T> mResponseClass;

	private Gson mGson;


	/**
	 * Constructor method for requests that return JSON responses.
	 * <p>
	 * This constructor will call {@link #GsonRequest(int, String, Class, Gson)} passing the parameters received for
	 * the first three ones, and the {@link Gson} object returned from {@link GsonUtils#getReusableGson()} as the last
	 * one.
	 *
	 * @param method
	 * 		Request method to use (see {@link com.android.volley.Request.Method} to see the possible options).
	 * @param url
	 * 		URL of the request.
	 * @param responseClass
	 * 		Class type of the object that will be returned as a response to the request.
	 */
	public GsonRequest(int method, String url, Class<T> responseClass) {
		this(method, url, responseClass, GsonUtils.getReusableGson());
	}

	/**
	 * Constructor method for requests that return JSON responses.
	 *
	 * @param method
	 * 		Request method to use (see {@link com.android.volley.Request.Method} to see the possible options).
	 * @param url
	 * 		URL of the request.
	 * @param responseClass
	 * 		Class type of the object that will be returned as a response to the request.
	 * @param gson
	 * 		{@link Gson} object that will be used to parse the response.
	 */
	public GsonRequest(int method, String url, Class<T> responseClass, Gson gson) {
		super(method, url);

		mResponseClass = responseClass;

		mGson = gson;
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

			return createSuccessResponse(response, mGson.fromJson(json, mResponseClass));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}

}
