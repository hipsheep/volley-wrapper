# Volley Wrapper
This project is a wrapper of the [Volley network library](http://developer.android.com/training/volley/index.html) for Android. The reason why I created it is because after using Volley for a couple of projects I realized I ended up implementing the same code over and over to be able to easily send requests and parse responses using Gson, so I decided to abstract all that code to a library that I can use on every project, and hopefully other people can use it too to make the interaction with Volley a bit easier.

# Dependencies
This project has the following dependencies:

* [Volley network library](http://developer.android.com/training/volley/index.html)
* [Gson library](https://github.com/google/gson)

Both libraries are already added to the project as Maven dependencies.

When you integrate Volley Wrapper into your app, you'll have to add the Gson dependency to your app's `build.gradle` file, as shown below:

```gradle
dependencies {
	// The Gson library is used to easily parse JSON objects
	compile 'com.google.code.gson:gson:2.4'
}
```

The Volley dependency will be implicitly added when you include the Volley Wrapper one (see the section below for more details).

# Import library from Maven
You can import Volley Wrapper into your project as a Maven dependency, as shown below:

```gradle
repositories {
	maven {
		// Include the URL of the Maven repo where the Volley Wrapper library is published. Release 
		// candidates are hosted on the "canary" channel and production releases on the "release" one. 
		// To switch between channels change the last part of this URL to "canary" or "release"
		url "https://raw.github.com/hipsheep/volley-wrapper-distribution/release"
	}
}

dependencies {
	// Import Volley Wrapper library
	compile('com.hipsheep:volleywrapper:1.0.0@aar') {
	        // Include Volley dependency
	        transitive = true;
    }
}
```

As mentioned on the previous section, the Volley library dependency will be implicitly added when you include the Volley Wrapper one. However, if you wish to add your own version of Volley you can exclude the implicit dependency created by Volley Wrapper. To do this you have to import Volley Wrapper like this:

```gradle
dependencies {
	// Import Volley Wrapper library
	compile 'com.hipsheep:volleywrapper:1.0.0-rc1@aar'
}
```

# Documentation
All the documentation on how to [setup](https://github.com/hipsheep/volley-wrapper/wiki/2.-Setup) and [use](https://github.com/hipsheep/volley-wrapper/wiki/3.-Usage) this library can be found in the [Wiki](https://github.com/hipsheep/volley-wrapper/wiki).

# License
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

> http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
