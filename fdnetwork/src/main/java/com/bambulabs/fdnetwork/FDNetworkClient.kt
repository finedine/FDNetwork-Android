package com.bambulabs.fdnetwork

import android.os.Build
import com.bambulabs.fdnetwork.FDNetworkCompanions.Companion.authenticationToken
import android.text.TextUtils
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.tls.HandshakeCertificates
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayInputStream
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
//
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
            
            val isgCert = """
                -----BEGIN CERTIFICATE-----
                MIIFazCCA1OgAwIBAgIRAIIQz7DSQONZRGPgu2OCiwAwDQYJKoZIhvcNAQELBQAw
                TzELMAkGA1UEBhMCVVMxKTAnBgNVBAoTIEludGVybmV0IFNlY3VyaXR5IFJlc2Vh
                cmNoIEdyb3VwMRUwEwYDVQQDEwxJU1JHIFJvb3QgWDEwHhcNMTUwNjA0MTEwNDM4
                WhcNMzUwNjA0MTEwNDM4WjBPMQswCQYDVQQGEwJVUzEpMCcGA1UEChMgSW50ZXJu
                ZXQgU2VjdXJpdHkgUmVzZWFyY2ggR3JvdXAxFTATBgNVBAMTDElTUkcgUm9vdCBY
                MTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAK3oJHP0FDfzm54rVygc
                h77ct984kIxuPOZXoHj3dcKi/vVqbvYATyjb3miGbESTtrFj/RQSa78f0uoxmyF+
                0TM8ukj13Xnfs7j/EvEhmkvBioZxaUpmZmyPfjxwv60pIgbz5MDmgK7iS4+3mX6U
                A5/TR5d8mUgjU+g4rk8Kb4Mu0UlXjIB0ttov0DiNewNwIRt18jA8+o+u3dpjq+sW
                T8KOEUt+zwvo/7V3LvSye0rgTBIlDHCNAymg4VMk7BPZ7hm/ELNKjD+Jo2FR3qyH
                B5T0Y3HsLuJvW5iB4YlcNHlsdu87kGJ55tukmi8mxdAQ4Q7e2RCOFvu396j3x+UC
                B5iPNgiV5+I3lg02dZ77DnKxHZu8A/lJBdiB3QW0KtZB6awBdpUKD9jf1b0SHzUv
                KBds0pjBqAlkd25HN7rOrFleaJ1/ctaJxQZBKT5ZPt0m9STJEadao0xAH0ahmbWn
                OlFuhjuefXKnEgV4We0+UXgVCwOPjdAvBbI+e0ocS3MFEvzG6uBQE3xDk3SzynTn
                jh8BCNAw1FtxNrQHusEwMFxIt4I7mKZ9YIqioymCzLq9gwQbooMDQaHWBfEbwrbw
                qHyGO0aoSCqI3Haadr8faqU9GY/rOPNk3sgrDQoo//fb4hVC1CLQJ13hef4Y53CI
                rU7m2Ys6xt0nUW7/vGT1M0NPAgMBAAGjQjBAMA4GA1UdDwEB/wQEAwIBBjAPBgNV
                HRMBAf8EBTADAQH/MB0GA1UdDgQWBBR5tFnme7bl5AFzgAiIyBpY9umbbjANBgkq
                hkiG9w0BAQsFAAOCAgEAVR9YqbyyqFDQDLHYGmkgJykIrGF1XIpu+ILlaS/V9lZL
                ubhzEFnTIZd+50xx+7LSYK05qAvqFyFWhfFQDlnrzuBZ6brJFe+GnY+EgPbk6ZGQ
                3BebYhtF8GaV0nxvwuo77x/Py9auJ/GpsMiu/X1+mvoiBOv/2X/qkSsisRcOj/KK
                NFtY2PwByVS5uCbMiogziUwthDyC3+6WVwW6LLv3xLfHTjuCvjHIInNzktHCgKQ5
                ORAzI4JMPJ+GslWYHb4phowim57iaztXOoJwTdwJx4nLCgdNbOhdjsnvzqvHu7Ur
                TkXWStAmzOVyyghqpZXjFaH3pO3JLF+l+/+sKAIuvtd7u+Nxe5AW0wdeRlN8NwdC
                jNPElpzVmbUq4JUagEiuTDkHzsxHpFKVK7q4+63SM1N95R1NbdWhscdCb+ZAJzVc
                oyi3B43njTOQ5yOf+1CceWxG1bQVs5ZufpsMljq4Ui0/1lvh+wjChP4kqKOJ2qxq
                4RgqsahDYVvTH9w7jXbyLeiNdd8XM2w9U/t7y0Ff/9yi0GE44Za4rF2LN9d11TPA
                mRGunUHBcnWEvgJBQl9nJEiU0Zsnvgc/ubhPgXRR4Xq37Z0j4r7g1SgEEzwxA57d
                emyPxgcYxn/eR44/KJ4EBs+lVDR3veyJm+kXQ99b21/+jh5Xos1AnX5iItreGCc=
                -----END CERTIFICATE-----
                """.trimIndent()
            val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
            val isgCertificate: Certificate? =
                cf.generateCertificate(ByteArrayInputStream(isgCert.toByteArray(charset("UTF-8"))))
            val certificates = HandshakeCertificates.Builder()
                .addTrustedCertificate(isgCertificate as X509Certificate)
                // Uncomment to allow connection to any site generally, but could possibly cause
                // noticeable memory pressure in Android apps.
                //              .addPlatformTrustedCertificates()
                .build()

            try {
                val httpClient = OkHttpClient.Builder()
                    .sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager())
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

            if (FDNetworkCompanions.isTestModeEnabled)
                httpClient.addInterceptor(FDNetworkCompanions.fdNetworkApplication?.let {
                    ChuckerInterceptor(
                        it
                    )
                })

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