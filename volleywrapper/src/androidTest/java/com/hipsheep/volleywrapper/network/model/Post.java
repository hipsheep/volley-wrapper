package com.hipsheep.volleywrapper.network.model;

import com.google.gson.annotations.SerializedName;
import com.hipsheep.volleywrapper.network.Params;

/**
 * Test response model returned from the backend as a JSON object.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
public class Post {

	@SerializedName(Params.KEY_USER_ID)
	private Integer mUserId;

	@SerializedName(Params.KEY_ID)
	private Integer mId;

	@SerializedName(Params.KEY_TITLE)
	private String mTitle;

	@SerializedName(Params.KEY_BODY)
	private String mBody;


	public Post(Integer userId, Integer id, String title, String body) {
		mUserId = userId;
		mId = id;
		mTitle = title;
		mBody = body;
	}

	public Integer getUserId() {
		return mUserId;
	}

	public Integer getId() {
		return mId;
	}

	public void setId(Integer id) {
		mId = id;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getBody() {
		return mBody;
	}

	@Override
	public boolean equals(Object o) {
		Post post = (Post) o;

		return post != null
				&& varsAreEqual(mUserId, post.getUserId())
				&& varsAreEqual(mId, post.getId())
				&& varsAreEqual(mTitle, post.getTitle())
				&& varsAreEqual(mBody, post.getBody());
	}

	private boolean varsAreEqual(Object o1, Object o2) {
		return o1 == null && o2 == null || o1 != null && o1.equals(o2);
	}
}
