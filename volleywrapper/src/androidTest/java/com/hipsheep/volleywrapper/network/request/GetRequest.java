package com.hipsheep.volleywrapper.network.request;

import com.hipsheep.volleywrapper.network.Params;
import com.hipsheep.volleywrapper.network.model.Post;
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
