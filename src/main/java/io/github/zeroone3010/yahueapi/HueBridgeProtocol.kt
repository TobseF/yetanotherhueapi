package io.github.zeroone3010.yahueapi

enum class HueBridgeProtocol(internal val protocol: String) {

  /**
   * A regular HTTP connection.
   */
  HTTP("http"),

  /**
   * An encrypted HTTPS connection. However, as the Bridge uses a self-signed certificate,
   * it is not possible to verify it. Using this enum value turns off the certificate
   * verification.
   */
  UNVERIFIED_HTTPS("https")
}
