package com.hipsheep.volleywrapper.java.network.request;

import com.hipsheep.volleywrapper.java.network.Params;
import com.hipsheep.volleywrapper.java.network.model.Post;
import com.hipsheep.volleywrapper.utils.GsonUtils;

/**
 * Test GET request that will be sent to the server.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
public class GetRequest extends GsonRequest<Post[]> {

	public GetRequest(String url, Integer userId) {
		super(Method.GET, url, Post[].class, GsonUtils.getGson());

		// Set query params
		addQueryParam(Params.KEY_USER_ID, userId);
	}

}
