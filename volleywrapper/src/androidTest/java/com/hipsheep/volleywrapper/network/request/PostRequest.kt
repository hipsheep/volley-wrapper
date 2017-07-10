package com.hipsheep.volleywrapper.network.request

import com.hipsheep.volleywrapper.network.Param
import com.hipsheep.volleywrapper.network.model.Post
import com.hipsheep.volleywrapper.utils.GsonUtils

/**
 * Test POST request that will be sent to the server.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
class PostRequest(url: String) : GsonRequest<Post>(Method.POST, url, Post::class.java, GsonUtils.gson) {

    constructor(url: String, userId: Int, title: String, body: String) : this(url) {
        // Add body params
        bodyParams.put(Param.Key.USER_ID, userId)
        bodyParams.put(Param.Key.TITLE, title)
        bodyParams.put(Param.Key.BODY, body)
    }

    constructor(url: String, body: String?) : this(url) {
        // Set request body
        this.body = body
    }

}