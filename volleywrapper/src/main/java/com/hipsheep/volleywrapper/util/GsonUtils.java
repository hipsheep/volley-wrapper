package com.hipsheep.volleywrapper.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Utility class used for {@link Gson} related actions.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
public class GsonUtils {

	private static Gson sGson;


	// Don't allow object creations for this class
	private GsonUtils() {}

	/**
	 * Returns a {@link Gson} object that is only created the first time this method is called. Subsequent calls to
	 * this method will return always the same object, which is saved in {@link #sGson}.
	 *
	 * @return A reusable {@link Gson} object.
	 */
	public static Gson getReusableGson() {
		if (sGson == null) {
			sGson = (new GsonBuilder()).create();
		}

		return sGson;
	}

}
