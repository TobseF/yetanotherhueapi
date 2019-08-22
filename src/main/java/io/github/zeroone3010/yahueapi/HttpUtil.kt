package io.github.zeroone3010.yahueapi

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

internal object HttpUtil {

  fun put(baseUrl: URL, path: String, body: String): String {
    return getString(baseUrl, path, body, "PUT")
  }

  fun post(baseUrl: URL, path: String, body: String): String {
    return getString(baseUrl, path, body, "POST")
  }

  private fun getString(baseUrl: URL, path: String, body: String, method: String): String {
    try {
      val connection = URL(baseUrl.toString() + path).openConnection() as HttpURLConnection
      connection.doOutput = true
      connection.requestMethod = method
      connection.setRequestProperty("Host", connection.url.host)
      connection.outputStream.use { outputStream ->
        OutputStreamWriter(outputStream, "UTF-8").use { writer ->
          writer.write(body)
          writer.flush()
        }
      }
      connection.connect()
      BufferedReader(InputStreamReader(connection.inputStream)).use { reader -> return reader.lineSequence().joinToString("\n") }
    } catch (e: IOException) {
      throw HueApiException(e)
    }

  }
}// prevent instantiation
