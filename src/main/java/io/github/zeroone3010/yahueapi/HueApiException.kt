package io.github.zeroone3010.yahueapi

class HueApiException : RuntimeException {
  constructor(cause: Throwable) : super(cause)

  constructor(message: String) : super(message)
}
