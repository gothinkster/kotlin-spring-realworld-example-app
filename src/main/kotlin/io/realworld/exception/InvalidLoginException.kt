package io.realworld.exception

class InvalidLoginException(val field: String, val error: String) : RuntimeException()
