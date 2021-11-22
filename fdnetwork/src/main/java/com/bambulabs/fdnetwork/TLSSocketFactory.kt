package com.bambulabs.fdnetwork

import android.os.Build
import androidx.annotation.Nullable
import okhttp3.tls.HandshakeCertificates
import java.io.ByteArrayInputStream
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

class TLSSocketFactory @Throws(
    KeyManagementException::class,
    NoSuchAlgorithmException::class,
    KeyStoreException::class
) constructor() : SSLSocketFactory() {

    private val delegate: SSLSocketFactory
    var trustManagers: Array<TrustManager> = emptyArray()

    override fun getDefaultCipherSuites(): Array<String> {
        return delegate.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.supportedCipherSuites
    }

    init {
        generateTrustManagers()
        val context = SSLContext.getInstance("TLS")
        context.init(null, trustManagers, null)
        delegate = context.socketFactory


    }

    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class)
    private fun generateTrustManagers() {
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
            ("Unexpected default trust managers:" + Arrays.toString(
                trustManagers
            ))
        }
        this.trustManagers = trustManagers
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        return enableTLSOnSocket(delegate.createSocket())
    }

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose))
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(host: String, port: Int): Socket {
        return enableTLSOnSocket(delegate.createSocket(host, port))
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(
        host: String,
        port: Int,
        localHost: InetAddress,
        localPort: Int
    ): Socket {
        return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort))
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket {
        return enableTLSOnSocket(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress,
        port: Int,
        localAddress: InetAddress,
        localPort: Int
    ): Socket {
        return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort))
    }

    private fun enableTLSOnSocket(socket: Socket): Socket {
        if (socket != null && (socket is SSLSocket)) {
            socket.enabledProtocols = arrayOf("TLSv1.1", "TLSv1.2")
        }
        return socket
    }

    @Nullable
    fun getTrustManager(): X509TrustManager {
        return trustManagers[0] as X509TrustManager
    }
}