package com.hipsheep.volleywrapper;

import com.android.volley.RetryPolicy;

import java.util.Map;

/**
 * Configuration that can be set to VolleyWrapper through the {@link VolleyWrapper#setDefaultConfiguration(Configuration)}
 * method.
 *
 * @author Franco Sabadini (fsabadi@gmail.com)
 */
public class Configuration {

	private RetryPolicy mRetryPolicy;

	private Boolean mShouldCache;

	private String mBodyContentType;

	private Map<String, String> mHeaders;


	public RetryPolicy getRetryPolicy() {
		return mRetryPolicy;
	}

	public void setRetryPolicy(RetryPolicy retryPolicy) {
		mRetryPolicy = retryPolicy;
	}

	public Boolean isShouldCache() {
		return mShouldCache;
	}

	public void setShouldCache(Boolean shouldCache) {
		mShouldCache = shouldCache;
	}

	public String getBodyContentType() {
		return mBodyContentType;
	}

	public void setBodyContentType(String bodyContentType) {
		mBodyContentType = bodyContentType;
	}

	public Map<String, String> getHeaders() {
		return mHeaders;
	}

	public void setHeaders(Map<String, String> headers) {
		mHeaders = headers;
	}

	/*
	 * Inner classes, interfaces, enums
	 */

	/**
	 * Builder class used to create {@link Configuration} objects.
	 */
	public static class Builder {

		private Configuration mConfiguration;


		public Builder() {
			mConfiguration = new Configuration();
		}

		/**
		 * Sets a retry policy to use as default for all requests.
		 *
		 * @param retryPolicy
		 * 		{@link RetryPolicy} to use for all requests.
		 */
		public Builder setRetryPolicy(RetryPolicy retryPolicy) {
			mConfiguration.setRetryPolicy(retryPolicy);

			return this;
		}

		/**
		 * Sets whether all requests should cache responses or not.
		 *
		 * @param shouldCache
		 * 		{@code true} if all requests should cache responses, or {@code false} otherwise.
		 */
		public Builder setShouldCache(Boolean shouldCache) {
			mConfiguration.setShouldCache(shouldCache);

			return this;
		}

		/**
		 * Sets a body content type to use as default for all requests.
		 *
		 * @param bodyContentType
		 * 		Body content type to use for all requests.
		 */
		public Builder setBodyContentType(String bodyContentType) {
			mConfiguration.setBodyContentType(bodyContentType);

			return this;
		}

		/**
		 * Sets the default headers to use for all requests.
		 *
		 * @param headers
		 * 		Default headers to use for all requests.
		 */
		public Builder setHeaders(Map<String, String> headers) {
			mConfiguration.setHeaders(headers);

			return this;
		}

		public Configuration build() {
			return mConfiguration;
		}

	}

}
