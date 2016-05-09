package com.hipsheep.volleywrapper;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.volley.VolleyError;
import com.hipsheep.volleywrapper.network.model.ResponseCallback;
import com.hipsheep.volleywrapper.network.request.GetRequest;
import com.hipsheep.volleywrapper.network.request.PostRequest;
import com.hipsheep.volleywrapper.network.model.Post;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class includes all the instrumented tests for the Volley Wrapper library.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
@RunWith(AndroidJUnit4.class)
public class VolleyWrapperTest {

	private static final String TAG = VolleyWrapperTest.class.getName();

	/**
	 * URL used for testing mocked results.
	 */
	private static final String TEST_URL_SUCCESS = "http://jsonplaceholder.typicode.com/posts";

	/**
	 * Fake URL used to get a 404 error as a response of sending a request to it.
	 */
	private static final String TEST_URL_FAIL = "http://jsonplaceholder.typicode.com/fake";

	/**
	 * Test model used to send info to the backend.
	 */
	private static final Post TEST_POST_MODEL = new Post(2, 101, "test title", "test body");


	/**
	 * Initializes the Volley Wrapper library before the tests are run.
	 */
	@BeforeClass
	public static void initVolleyWrapper() {
		VolleyWrapper.init(InstrumentationRegistry.getTargetContext());
		VolleyWrapper.setDefaultBodyContentType("application/json");
	}

	@Test
	public void sendRequest_GET_Sync_Success() {
		int userId = 1;
		Post[] posts = null;

		try {
			posts = VolleyWrapper.sendRequest(new GetRequest(TEST_URL_SUCCESS, userId));
		} catch (Exception e) {
			Log.e(TAG, "GET request failed", e);
		}

		assertGetResponse(posts, userId);
	}

	@Test
	public void sendRequest_GET_Async_Success() {
		final int userId = 1;

		VolleyWrapper.sendRequest(new GetRequest(TEST_URL_SUCCESS, userId), new ResponseCallback<Post[]>() {

			@Override
			public void onSuccess(Post[] posts) {
				assertGetResponse(posts, userId);
			}

			@Override
			public void onFailure(VolleyError volleyError) {
				Log.e(TAG, "GET request failed", volleyError);

				Assert.fail();
			}

		});

		waitForResponse();
	}

	/**
	 * Asserts the successful response from the GET requests is correct.
	 */
	private void assertGetResponse(Post[] posts, int userId) {
		Assert.assertNotNull(posts);

		for (Post p : posts) {
			if (p.getUserId() != userId) {
				Assert.fail();
			}
		}
	}

	@Test
	public void sendRequest_GET_Sync_Fail_404() {
		try {
			VolleyWrapper.sendRequest(new GetRequest(TEST_URL_FAIL, null));

			// We are testing that the request should return an error, so if it doesn't then the test failed
			Assert.fail();
		} catch (Exception e) {
			assertError(404, e.getCause());
		}
	}

	@Test
	public void sendRequest_GET_Async_Fail_404() {
		VolleyWrapper.sendRequest(new GetRequest(TEST_URL_FAIL, null), new ResponseCallback<Post[]>() {

			@Override
			public void onSuccess(Post[] posts) {
				// We are testing that the request should return an error, so if it doesn't then the test failed
				Assert.fail();
			}

			@Override
			public void onFailure(VolleyError volleyError) {
				assertError(404, volleyError);
			}

		});

		waitForResponse();
	}

	@Test
	public void sendRequest_POST_Sync_Success() {
		Post post = null;

		try {
			post = VolleyWrapper.sendRequest(new PostRequest(TEST_URL_SUCCESS, TEST_POST_MODEL.getUserId(),
					TEST_POST_MODEL.getTitle(), TEST_POST_MODEL.getBody()));
		} catch (Exception e) {
			Log.e(TAG, "POST request failed", e);
		}

		assertPostResponse(post);
	}

	@Test
	public void sendRequest_POST_Async_Success() {
		VolleyWrapper.sendRequest(new PostRequest(TEST_URL_SUCCESS, TEST_POST_MODEL.getUserId(), TEST_POST_MODEL.getTitle(),
				TEST_POST_MODEL.getBody()), new ResponseCallback<Post>() {

			@Override
			public void onSuccess(Post post) {
				assertPostResponse(post);
			}

			@Override
			public void onFailure(VolleyError volleyError) {
				Log.e(TAG, "POST request failed", volleyError);

				Assert.fail();
			}

		});

		waitForResponse();
	}

	/**
	 * Asserts the successful response from the POST requests is correct.
	 */
	private void assertPostResponse(Post postResponse) {
		Assert.assertNotNull(postResponse);

		// Set post ID which is generated randomly by the backend (needed for the equals() method to work)
		TEST_POST_MODEL.setId(postResponse.getId());

		Assert.assertEquals(TEST_POST_MODEL, postResponse);
	}

	@Test
	public void sendRequest_POST_Sync_Fail_404() {
		try {
			VolleyWrapper.sendRequest(new PostRequest(TEST_URL_FAIL, null));

			// We are testing that the request should return an error, so if it doesn't then the test failed
			Assert.fail();
		} catch (Exception e) {
			assertError(404, e.getCause());
		}
	}

	@Test
	public void sendRequest_POST_Async_Fail_404() {
		VolleyWrapper.sendRequest(new PostRequest(TEST_URL_FAIL, null), new ResponseCallback<Post>() {

			@Override
			public void onSuccess(Post post) {
				// We are testing that the request should return an error, so if it doesn't then the test failed
				Assert.fail();
			}

			@Override
			public void onFailure(VolleyError volleyError) {
				assertError(404, volleyError);
			}

		});

		waitForResponse();
	}

	/**
	 * Asserts whether the error code inside the exception is the same as the one sent as parameter.
	 */
	private void assertError(int errorCode, Throwable volleyError) {
		Log.e(TAG, "Request failed", volleyError);

		if (volleyError instanceof VolleyError) {
			Assert.assertEquals(errorCode, ((VolleyError) volleyError).networkResponse.statusCode);
		} else {
			Assert.fail();
		}
	}

	/**
	 * Puts the current thread to sleep, which is needed for async responses to be able to return a value asynchronously without
	 * the test ending.
	 */
	private void waitForResponse() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
