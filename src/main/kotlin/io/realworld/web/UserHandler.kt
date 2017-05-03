package io.realworld.web

import io.realworld.exception.ForbiddenRequestException
import io.realworld.exception.UnauthorizedException
import io.realworld.jwt.ApiKeySecured
import io.realworld.model.User
import io.realworld.model.inout.Login
import io.realworld.model.inout.Register
import io.realworld.repository.UserRepository
import io.realworld.service.UserService
import org.mindrot.jbcrypt.BCrypt
import org.springframework.web.bind.annotation.*

@RestController
class UserHandler(val repository: UserRepository,
                  val service: UserService) {

    @PostMapping("/api/users/login")
    fun login(@RequestBody login: Login): Any {
        repository.findByEmail(login.email)?.let {
            if (BCrypt.checkpw(login.password, it.password)) {
                return view(service.updateToken(it))
            }
        }
        throw UnauthorizedException()
    }

    @PostMapping("/api/users")
    fun register(@RequestBody register: Register): Any {
        if (repository.existsByEmail(register.email))
            throw ForbiddenRequestException()

        val user = User(username = register.username,
                email = register.email, password = BCrypt.hashpw(register.password, BCrypt.gensalt()))
        user.token = service.newToken(user)

        return view(repository.save(user))
    }

    @ApiKeySecured
    @GetMapping("/api/user")
    fun currentUser() = view(service.currentUser())

    @ApiKeySecured
    @PutMapping("/api/user")
    fun updateUser(@RequestBody user: User): Any {
        val u = service.currentUser().copy(email = user.email, username = user.username,
                password = BCrypt.hashpw(user.password, BCrypt.gensalt()), image = user.image, bio = user.bio)
        u.token = service.newToken(u)

        return view(repository.save(u))
    }

    fun view(user: User) = mapOf("user" to user)
}