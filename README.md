# Groovy TinEye API

Basic wrapper for TinEye API (not finished)

API uses `Task` class from [RedUtilities](https://raw.githubusercontent.com/Redstonneur1256/RedUtilities) for requests

Example:

```groovy
// Obtain an API instance:
TinEyeAPI api = TinEyeAPIFactory.create("privateKey", "publicKey")

// Get the remaining requests count:
api.getRemainingSearches() // Task<Integer>

// Search an image from URL:
api.searchImage("http://tineye.com/images/meloncat.jpg") // Task<SearchResult>

// Shutdown all threads used by the API
api.disable()
```
