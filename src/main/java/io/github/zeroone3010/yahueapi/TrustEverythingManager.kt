package io.github.zeroone3010.yahueapi

import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.logging.Logger
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

internal class TrustEverythingManager : X509TrustManager {

  override fun getAcceptedIssuers(): Array<X509Certificate> {
    return arrayOf()
  }

  override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {
    // Do nothing
  }

  override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {
    // Do nothing
  }

  companion object {
    private val logger = Logger.getLogger("TrustEverythingManager")

    fun trustAllSslConnectionsByDisablingCertificateVerification() {
      try {
        logger.fine("Turning off certificate verification...")
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(TrustEverythingManager()), SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, session -> true }
        logger.fine("Certificate verification has been turned off, all certificates are now accepted.")
      } catch (e: NoSuchAlgorithmException) {
        throw HueApiException(e)
      } catch (e: KeyManagementException) {
        throw HueApiException(e)
      }

    }
  }
}
