package com.hipsheep.volleywrapper

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.android.volley.VolleyError
import com.hipsheep.volleywrapper.network.model.Post
import com.hipsheep.volleywrapper.network.model.ResponseCallback
import com.hipsheep.volleywrapper.network.request.GetRequest
import com.hipsheep.volleywrapper.network.request.PostRequest
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class includes all the instrumented tests for the Volley Wrapper library.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
@RunWith(AndroidJUnit4::class)
class VolleyWrapperTest {

    private val LOG_TAG = javaClass.name

    /**
     * URL used for testing mocked results.
     */
    private val TEST_URL_SUCCESS = "http://jsonplaceholder.typicode.com/posts"

    /**
     * Fake URL used to get a 404 error as a response of sending a request to it.
     */
    private val TEST_URL_FAIL = "http://jsonplaceholder.typicode.com/fake"

    /**
     * Test model used to send info to the backend.
     */
    private val TEST_POST_MODEL = Post(2, 101, "test title", "test body")


    companion object {

        /**
         * Initializes the Volley Wrapper library before the tests are run.
         */
        @BeforeClass @JvmStatic
        fun initVolleyWrapper() {
            VolleyWrapper.init(InstrumentationRegistry.getTargetContext())

            VolleyWrapper.defaultConfig = Configuration(bodyContentType = "application/json", syncTimeout = 30L)
        }

    }

    @Test
    fun sendRequest_GET_Sync_Success() {
        val userId = 1
        var posts: Array<Post>? = null

        try {
            posts = VolleyWrapper.sendRequest(GetRequest(TEST_URL_SUCCESS, userId))
        } catch (e: Exception) {
            Log.e(LOG_TAG, "GET request failed", e)
        }

        assertGetResponse(posts, userId)
    }

    @Test
    fun sendRequest_GET_Async_Success() {
        val userId = 1

        VolleyWrapper.sendRequest(GetRequest(TEST_URL_SUCCESS, userId), object : ResponseCallback<Array<Post>> {

            override fun onSuccess(data: Array<Post>?) {
                assertGetResponse(data, userId)
            }

            override fun onFailure(volleyError: VolleyError?) {
                Log.e(LOG_TAG, "GET request failed", volleyError)

                Assert.fail()
            }

        })

        waitForResponse()
    }

    /**
     * Asserts the successful response from the GET requests is correct.
     */
    private fun assertGetResponse(posts: Array<Post>?, userId: Int) {
        Assert.assertNotNull(posts)

        if (posts!!.any { it.userId != userId }) {
            Assert.fail()
        }
    }

    @Test
    fun sendRequest_GET_Sync_Fail_404() {
        try {
            VolleyWrapper.sendRequest(GetRequest(TEST_URL_FAIL, -1))

            // We are testing that the request should return an error, so if it doesn't then the test failed
            Assert.fail()
        } catch (e: Exception) {
            assertError(404, e.cause)
        }
    }

    @Test
    fun sendRequest_GET_Async_Fail_404() {
        VolleyWrapper.sendRequest(GetRequest(TEST_URL_FAIL, -1), object : ResponseCallback<Array<Post>> {

            override fun onSuccess(data: Array<Post>?) {
                // We are testing that the request should return an error, so if it doesn't then the test failed
                Assert.fail()
            }

            override fun onFailure(volleyError: VolleyError?) {
                assertError(404, volleyError)
            }

        })

        waitForResponse()
    }

    @Test
    fun sendRequest_POST_Sync_Success() {
        var post: Post? = null

        try {
            post = VolleyWrapper.sendRequest(PostRequest(TEST_URL_SUCCESS, TEST_POST_MODEL.userId,
                    TEST_POST_MODEL.title, TEST_POST_MODEL.body))
        } catch (e: Exception) {
            Log.e(LOG_TAG, "POST request failed", e)
        }

        assertPostResponse(post)
    }

    @Test
    fun sendRequest_POST_Async_Success() {
        VolleyWrapper.sendRequest(PostRequest(TEST_URL_SUCCESS, TEST_POST_MODEL.userId, TEST_POST_MODEL.title,
                TEST_POST_MODEL.body), object : ResponseCallback<Post> {

            override fun onSuccess(data: Post?) {
                assertPostResponse(data)
            }

            override fun onFailure(volleyError: VolleyError?) {
                Log.e(LOG_TAG, "POST request failed", volleyError)

                Assert.fail()
            }

        })

        waitForResponse()
    }

    /**
     * Asserts the successful response from the POST requests is correct.
     */
    private fun assertPostResponse(postResponse: Post?) {
        Assert.assertNotNull(postResponse)

        // Set post ID which is generated randomly by the backend (needed for the equals() method to work)
        TEST_POST_MODEL.id = postResponse?.id

        Assert.assertEquals(TEST_POST_MODEL, postResponse)
    }

    @Test
    fun sendRequest_POST_Sync_Fail_404() {
        try {
            VolleyWrapper.sendRequest(PostRequest(TEST_URL_FAIL, null))

            // We are testing that the request should return an error, so if it doesn't then the test failed
            Assert.fail()
        } catch (e: Exception) {
            assertError(404, e.cause)
        }
    }

    @Test
    fun sendRequest_POST_Async_Fail_404() {
        VolleyWrapper.sendRequest(PostRequest(TEST_URL_FAIL, null), object : ResponseCallback<Post> {

            override fun onSuccess(data: Post?) {
                // We are testing that the request should return an error, so if it doesn't then the test failed
                Assert.fail()
            }

            override fun onFailure(volleyError: VolleyError?) {
                assertError(404, volleyError)
            }

        })

        waitForResponse()
    }

    /**
     * Asserts whether the error code inside the exception is the same as the one sent as parameter.
     */
    private fun assertError(errorCode: Int, volleyError: Throwable?) {
        Log.e(LOG_TAG, "Request failed", volleyError)

        if (volleyError is VolleyError) {
            Assert.assertEquals(errorCode.toLong(), volleyError.networkResponse.statusCode.toLong())
        } else {
            Assert.fail()
        }
    }

    /**
     * Puts the current thread to sleep, which is needed for async responses to be able to return a value asynchronously without
     * the test ending.
     */
    private fun waitForResponse() {
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}