package pl.michalkowol.nip

sealed class NipServiceException(message: String, cause: Throwable? = null) : Exception(message, cause)

class EmptyResponseBodyException(message: String = "Empty response body received from REGON service") : NipServiceException(message)

class SessionExtractionException(message: String, cause: Throwable? = null) : NipServiceException(message, cause)

class NoLoginResultException(message: String = "No login result found in SOAP response") : NipServiceException(message)
