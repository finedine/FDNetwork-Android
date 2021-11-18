package com.bambulabs.fdnetwork

import android.os.Build
import com.bambulabs.fdnetwork.FDNetworkCompanions.Companion.authenticationToken
import android.text.TextUtils
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object FDNetworkClient {

    private var retrofit: Retrofit? = null
    private const val REQUEST_TIMEOUT = 3
    private var okHttpClient: OkHttpClient? = null

    fun getClient(url: String): Retrofit {

        if (okHttpClient == null)
            initOkHttp()

        if (retrofit == null || retrofit?.baseUrl().toString() != url) {
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient!!)
                .build()
        }
        return retrofit!!
    }

    private fun initOkHttp() {


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            try {
                val tlsSocketFactory = TLSSocketFactory()
                val httpClient = OkHttpClient.Builder()
                    .ignoreAllSSLErrors()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)

                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                httpClient.addInterceptor(interceptor)
                httpClient.addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")

                    // Adding Authorization token (API Key)
                    // Requests will be denied without API key
                    if (!TextUtils.isEmpty(authenticationToken)) {
                        authenticationToken?.let { requestBuilder.addHeader("Authorization", it) }
                    }

                    val request = requestBuilder.build()
                    chain.proceed(request)
                }

                httpClient.addInterceptor { chain ->
                    val request = chain.request()
                    // try the request
                    var response = chain.proceed(request)
                    var tryCount = 0
                    while (!response.isSuccessful && tryCount < 3) {
                        Log.d("intercept", "Request is not successful - $tryCount")
                        tryCount++
                        // retry the request
                        response = chain.proceed(request)
                    }
                    // otherwise just pass the original response on
                    response
                }

                okHttpClient = httpClient.build()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyStoreException) {
                e.printStackTrace()
            }
        } else {
            val httpClient = OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.MINUTES)

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClient.addInterceptor(interceptor)
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")

                // Adding Authorization token (API Key)
                // Requests will be denied without API key
                if (!TextUtils.isEmpty(authenticationToken)) {
                    authenticationToken?.let { requestBuilder.addHeader("Authorization", it) }
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }

            httpClient.addInterceptor { chain ->
                val request = chain.request()
                // try the request
                var response = chain.proceed(request)
                var tryCount = 0
                while (!response.isSuccessful && tryCount < 3) {
                    Log.d("intercept", "Request is not successful - $tryCount")
                    tryCount++
                    // retry the request
                    response = chain.proceed(request)
                }
                // otherwise just pass the original response on
                response
            }

            okHttpClient = httpClient.build()
        }
    }
    fun OkHttpClient.Builder.ignoreAllSSLErrors(): OkHttpClient.Builder {
        val naiveTrustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        }

        val insecureSocketFactory = SSLContext.getInstance("TLSv1.2").apply {
            val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory

        sslSocketFactory(insecureSocketFactory, naiveTrustManager)
        hostnameVerifier(HostnameVerifier { _, _ -> true })
        return this
    }
}