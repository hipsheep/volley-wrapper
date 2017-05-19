# Volley Wrapper
This project is a wrapper of the [Volley network library](http://developer.android.com/training/volley/index.html) for Android. The reason why I created it is because after using *Volley* for a couple of projects I realized I ended up implementing the same code over and over to be able to easily send requests and parse responses using *Gson*, so I decided to abstract all that code to a library that I can use on every project, and hopefully other people can use it too to make the interaction with *Volley* a bit easier.

# Dependencies
This project has the following library dependencies:

* [Volley](http://developer.android.com/training/volley/index.html)
* [Gson](https://github.com/google/gson)

Both libraries will be implicitly added when you include *Volley Wrapper* in your `build.gradle` file (see the section below for more details).

# Import library from Maven
*Volley Wrapper* is distributed as an *aar* file from [JCenter](https://bintray.com/bintray/jcenter). You can import the library into your project as a Maven dependency in your app's `build.gradle` file:

```gradle
dependencies {
	// Import Volley Wrapper library
	compile('com.hipsheep:volley-wrapper:1.1.0@aar') {
		// Include Volley and Gson dependencies
		transitive = true;
	}
}
```

As mentioned on the previous section, the *Volley* and *Gson* dependencies will be implicitly added when you include *Volley Wrapper*. However, if you wish to add your own versions of *Volley* and *Gson* you can exclude the implicit dependencies. To do this, you have to import *Volley Wrapper* as shown below:

```gradle
dependencies {
	// Import Volley Wrapper library
	compile 'com.hipsheep:volley-wrapper:1.1.0@aar'
}
```

# Documentation
All the documentation on how to [setup](https://github.com/hipsheep/volley-wrapper/wiki/2.-Setup) and [use](https://github.com/hipsheep/volley-wrapper/wiki/3.-Usage) this library can be found in the [Wiki](https://github.com/hipsheep/volley-wrapper/wiki).

# Contribution
If you want to propose a change, report a bug or just ask a question you can do it by creating an [issue](https://github.com/hipsheep/volley-wrapper/issues). If you want to contribute with code changes you can create a [pull request](https://github.com/hipsheep/volley-wrapper/pulls) to the _develop_ branch (please make sure to create an issue before working on a PR so we can discuss whether the change proposed makes sense to be added to the library).

# License
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

> http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
