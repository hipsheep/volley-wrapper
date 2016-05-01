# IMPORTANT
This library is under construction, I pushed a first release candidate version of it but I haven't had time to test it yet, I will do so on the next few days and remove this message. I will also add more documentation on how to use it then.

# Volley Wrapper
This project is a wrapper of the [Volley network library](http://developer.android.com/training/volley/index.html) for Android. The reason why I created it is because after using Volley for a couple of projects I realized I ended up implementing the same code over and over to be able to easily send requests and parse responses using Gson, so I decided to abstract all that code to a library that I can use on every project, and hopefully other people can use it too to make the interaction with Volley a bit easier.

# Dependencies
This project has the following dependencies:

* [Volley network library](http://developer.android.com/training/volley/index.html) (already included in the project, as an aar).
* [Gson library](https://github.com/google/gson) (already included in the project, as a Maven dependency).

When you integrate Volley Wrapper into your app as an aar file, you'll have to add these dependencies to your project.

# How to include the library
To include Volley Wrapper into your app clone the repository into your machine and build the project using Gradle. This will create an aar file, that you can include into your project in Android Studio.

# Usage 
On this section I will explain how to use Volley Wrapper in your project.

## Initialisation
The first thing you'll need to do before you start using Volley Wrapper to send requests is to initialise it. This can be done by using one of the methods shown below. 

* If you want Volley Wrapper to create a default `RequestQueue`, then use this method:

```java
VolleyWrapper.init(context);
```

* If you want to set your own `RequestQueue`, then use this method:

```java
VolleyWrapper.init(requestQueue);
```

## Creating requests
Before you start sending requets using Volley Wrapper you have to create them. All requests should be created as classes that extend from `BaseRequest` or, if your request will return JSON object as a response and you want to use Gson to parse it, you can extend from `GsonRequest`.

I will show 2 examples below, on how to create a GET request and a POST one. These examples are obviously not of useful requests, they are just to give you an idea of how to structure your requets, set the URL, add params, etc. On a real project, you might want to create a different request class for each request you need to call (to keep a clean and well structured code base), but that is not needed if you prefer a different approach.

### GET request example

```java
/**
 * GET request that will be sent to the server.
 */
public class GetRequest extends GsonRequest<GetResponse> {

	public GetRequest(String param1Value, int param2Value) {
		super(Method.GET, "http://www.fakeGETrequesturl.com", GetResponse.class, GsonUtils.getGson());

		// Set query params
		addQueryParam("para1key", param1Value);
		addQueryParam("para2key", param2Value);
	}

}

/**
 * Response model returned from the GET request as a JSON object.
 */
public class GetResponse {

	// SerializedName() sets the name of the parameter key that is returned from the server inside the JSON object
	@SerializedName("param1")
	private String mParam1;

	// SerializedName() sets the name of the parameter key that is returned from the server inside the JSON object
	@SerializedName("param2")
	private int mParam2;


	public String getParam1() {
		return mParam1;
	}

	public int getParam2() {
		return mParam2;
	}

}
```

### POST request example

```java
/**
 * POST request that will be sent to the server.
 */
public class PostRequest extends GsonRequest<PostResponse> {

	public PostRequest(String param1Value, int param2Value) {
		this();

		// Add body params
		addBodyParam("para1key", param1Value);
		addBodyParam("para2key", param2Value);
	}

	public PostRequest(String body) {
		this();

		// Set request body
		setBody(body);
	}

	public PostRequest() {
		super(Method.POST, "http://www.fakePOSTrequesturl.com", PostResponse.class, GsonUtils.getGson());
	}

}

/**
 * Response model returned from the POST request as a JSON object.
 */
public class PostResponse {

	// SerializedName() sets the name of the parameter key that is returned from the server inside the JSON object
	@SerializedName("param1")
	private String mParam1;

	// SerializedName() sets the name of the parameter key that is returned from the server inside the JSON object
	@SerializedName("param2")
	private int mParam2;


	public String getParam1() {
		return mParam1;
	}

	public int getParam2() {
		return mParam2;
	}

}
```

## Sending requests
Once Volley Wrapper has being initilised and you have created your requests you can start sending them to the server. To do so, you can use one of the methods shown below.

* If you want to send an asynchronous request and receive the response on a callback, use the following method:

```java
VolleyWrapper.sendRequest(request, responseCallback);
```

* If you want to send a synchronous request and receive the response as a result of the method you call, use the following method (**IMPORTANT:** don't call this method on the Main Thread or the app will freeze):

```java
VolleyWrapper.sendRequest(request);
```

# Changelog
// TODO

# License
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

> http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
