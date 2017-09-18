package com.hipsheep.volleywrapper.network.request;

import com.hipsheep.volleywrapper.network.Params;
import com.hipsheep.volleywrapper.network.model.Post;
import com.hipsheep.volleywrapper.utils.GsonUtils;

/**
 * Test POST request that will be sent to the server.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
public class PostRequest extends GsonRequest<Post> {

	public PostRequest(String url, Integer userId, String title, String body) {
		this(url);

		// Add body params
		addBodyParam(Params.KEY_USER_ID, userId);
		addBodyParam(Params.KEY_TITLE, title);
		addBodyParam(Params.KEY_BODY, body);
	}

	public PostRequest(String url, String body) {
		this(url);

		// Set request body
		setBody(body);
	}

	public PostRequest(String url) {
		super(Method.POST, url, Post.class, GsonUtils.getGson());
	}

}
