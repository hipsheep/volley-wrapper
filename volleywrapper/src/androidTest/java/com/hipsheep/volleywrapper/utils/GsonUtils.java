package com.hipsheep.volleywrapper.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
public class GsonUtils {

	private static final Gson GSON = new GsonBuilder().create();


	public static Gson getGson() {
		return GSON;
	}

}
