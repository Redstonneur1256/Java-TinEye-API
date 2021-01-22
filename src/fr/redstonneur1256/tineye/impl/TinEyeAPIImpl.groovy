package fr.redstonneur1256.tineye.impl

import fr.redstonneur1256.redutilities.Utils
import fr.redstonneur1256.redutilities.Validate
import fr.redstonneur1256.redutilities.async.Task
import fr.redstonneur1256.redutilities.async.ThreadPool
import fr.redstonneur1256.redutilities.async.pools.ReusableThreadPool
import fr.redstonneur1256.redutilities.function.UnsafeProvider
import fr.redstonneur1256.redutilities.io.Http
import fr.redstonneur1256.tineye.ImageInformation
import fr.redstonneur1256.tineye.SearchResult
import fr.redstonneur1256.tineye.TinEyeAPI
import groovy.json.JsonSlurper

import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@SuppressWarnings('GrUnresolvedAccess')
class TinEyeAPIImpl implements TinEyeAPI {

    static final String defaultAPIUrl
    static final JsonSlurper json

    static {
        defaultAPIUrl = "https://api.tineye.com/rest/"
        json = new JsonSlurper()
    }

    private String privateKey
    private String publicKey
    private String apiURL
    private SecretKey privateKeyInstance
    private ThreadPool pool

    TinEyeAPIImpl(String privateKey, String publicKey) {
        this(privateKey, publicKey, defaultAPIUrl)
    }

    TinEyeAPIImpl(String privateKey, String publicKey, String apiURL) {
        this.privateKey = privateKey
        this.publicKey = publicKey
        this.apiURL = apiURL
        this.privateKeyInstance = new SecretKeySpec(privateKey.getBytes(), "HmacSHA256")
        this.pool = new ReusableThreadPool()
    }

    /**
     * Please note that the API can still be used again after calling this method
     * (The method will only stop currently running API threads)
     */
    @Override
    void shutdown() {
        pool.shutdown()
    }

    @Override
    Task<SearchResult> searchImage(String imageURL) {
        Validate.nonNull(imageURL, "imageURL")

        return pool.execute((UnsafeProvider<SearchResult, Throwable>) { ->
            Map<String, Object> params = new HashMap<>()
            params.image_url = URLEncoder.encode(imageURL, 'UTF-8')
            Object result = executeRequest('search/', Http.Method.get, params)

            int total = result.stats.total_results as int
            Object[] matches = result.results.matches as Object[]
            ImageInformation[] images = new ImageInformation[matches.length]

            for (i in 0..<images.length) {
                Object raw = matches[i]
                Object backlink = raw.backlinks

                ImageInformation information = new ImageInformation(
                        raw.domain as String,
                        new ImageInformation.BackLink(backlink.url as String, backlink.crawl_date as String, backlink.backlink as String),
                        raw.format as String,
                        raw.width as int,
                        raw.height as int,
                        raw.score as int,
                        raw.image_url as String,
                        raw.filesize as long
                )

                images[i] = information
            }

            return new SearchResult(total, images)
        })
    };

    @Override
    Task<Integer> getRemainingSearches() {
        return pool.execute((UnsafeProvider<Integer, Throwable>) { ->
            Object result = executeRequest('remaining_searches/', Http.Method.get, new HashMap<>())

            return result.results.total_remaining_searches
        })
    };

    private Object executeRequest(String endPoint, Http.Method method, Map<String, Object> parameters) {
        long date = (long) (System.currentTimeMillis() / 1000)

        String url = "$apiURL$endPoint"
        String nonce = UUID.randomUUID().toString()

        String signature = generateSignature(url, method, date, nonce, parameters)

        parameters.api_key = publicKey
        parameters.api_sig = signature
        parameters.date = date
        parameters.nonce = nonce
        String params = joinParams(parameters)

        String raw = Http.url(url + params).read()
        Object result = json.parseText(raw)

        if (result.code != 200) {
            throw new IllegalStateException((result.messages as String[]) [1])
        }

        return result
    };

    private String generateSignature(String url, Http.Method method, long date, String nonce, Map<String, Object> parameters) {
        String params = parameters.isEmpty() ? '' : joinParams(parameters).substring(1)

        String string = privateKey + method.name().toUpperCase() + date + nonce + url + params

        Mac mac = Mac.getInstance('HmacSHA256')
        mac.init(privateKeyInstance)
        mac.update(string.getBytes())
        return Utils.binaryToHex(mac.doFinal()).toLowerCase()
    }

    private static String joinParams(Map<String, Object> params) {
        if (params.isEmpty()) {
            return ""
        }
        StringBuilder builder = new StringBuilder()

        for (entry in params) {
            if (builder.length() != 0) {
                builder.append('&')
            }

            builder.append(entry.key).append('=').append(entry.value)
        }

        return '?' + builder.toString()
    }

}
