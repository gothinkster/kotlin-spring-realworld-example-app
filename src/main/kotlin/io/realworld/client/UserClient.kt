package io.realworld.client

import feign.RequestLine
import io.realworld.model.User
import io.realworld.model.inout.Login
import io.realworld.model.inout.Register

interface UserClient {
    @RequestLine("POST /api/users/login")
    fun login(login: Login): User

    @RequestLine("POST /api/users")
    fun register(register: Register): User
}
