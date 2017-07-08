package com.hipsheep.volleywrapper.network.request

import com.hipsheep.volleywrapper.network.Param
import com.hipsheep.volleywrapper.network.model.Post
import com.hipsheep.volleywrapper.utils.GsonUtils

/**
 * Test GET request that will be sent to the server.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
class GetRequest(url: String, userId: Int) : GsonRequest<Array<Post>>(Method.GET, url, GsonUtils.gson) {

    init {
        addQueryParam(Param.Key.USER_ID, userId)
    }

}