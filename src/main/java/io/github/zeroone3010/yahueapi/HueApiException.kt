package io.github.zeroone3010.yahueapi

internal class HueApiException : RuntimeException {
  constructor(cause: Throwable) : super(cause)

  constructor(message: String) : super(message)
}
