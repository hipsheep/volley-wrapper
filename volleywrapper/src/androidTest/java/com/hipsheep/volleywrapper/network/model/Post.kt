package com.hipsheep.volleywrapper.network.model

import com.google.gson.annotations.SerializedName
import com.hipsheep.volleywrapper.network.Param

/**
 * Test response model returned from the backend as a JSON object.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
data class Post(@SerializedName(Param.Key.USER_ID) val userId: Int,
                @SerializedName(Param.Key.ID) var id: Int?,
                @SerializedName(Param.Key.TITLE) val title: String,
                @SerializedName(Param.Key.BODY) val body: String)