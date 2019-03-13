package br.alexandregpereira.amaro.exception

class ConnectionException(val error: ConnectionError) : Exception()