package io.realworld.web

import io.realworld.exception.*
import io.realworld.jwt.ApiKeySecured
import io.realworld.model.User
import io.realworld.model.inout.Login
import io.realworld.model.inout.Register
import io.realworld.model.inout.UpdateUser
import io.realworld.repository.UserRepository
import io.realworld.service.UserService
import org.mindrot.jbcrypt.BCrypt
import org.springframework.validation.BindException
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserHandler(val repository: UserRepository,
                  val service: UserService) {

    @PostMapping("/api/users/login")
    fun login(@Valid @RequestBody login: Login, errors: Errors): Any {
        InvalidRequest.check(errors)

        try {
            service.login(login)?.let {
                return view(service.updateToken(it))
            }
            return ForbiddenRequestException()
        } catch (e: InvalidLoginException) {
            val errors = org.springframework.validation.BindException(this, "")
            errors.addError(FieldError("", e.field, e.error))
            throw InvalidException(errors)
        }
    }

    @PostMapping("/api/users")
    fun register(@Valid @RequestBody register: Register, errors: Errors): Any {
        InvalidRequest.check(errors)

        // check for duplicate user
        val errors = org.springframework.validation.BindException(this, "")
        checkUserAvailability(errors, register.email, register.username)
        InvalidRequest.check(errors)

        val user = User(username = register.username!!,
                email = register.email!!, password = BCrypt.hashpw(register.password, BCrypt.gensalt()))
        user.token = service.newToken(user)

        return view(repository.save(user))
    }

    @ApiKeySecured
    @GetMapping("/api/user")
    fun currentUser() = view(service.currentUser())

    @ApiKeySecured
    @PutMapping("/api/user")
    fun updateUser(@Valid @RequestBody user: UpdateUser, errors: Errors): Any {
        InvalidRequest.check(errors)

        val currentUser = service.currentUser()

        // check for errors
        val errors = org.springframework.validation.BindException(this, "")
        if (currentUser.email != user.email && user.email != null) {
            if (repository.existsByEmail(user.email!!)) {
                errors.addError(FieldError("", "email", "already taken"))
            }
        }
        if (currentUser.username != user.username && user.username != null) {
            if (repository.existsByUsername(user.username!!)) {
                errors.addError(FieldError("", "username", "already taken"))
            }
        }
        if (user.password == "") {
            errors.addError(FieldError("", "password", "can't be empty"))
        }
        InvalidRequest.check(errors)

        // update the user
        val u = currentUser.copy(email = user.email ?: currentUser.email, username = user.username ?: currentUser.username,
                password = BCrypt.hashpw(user.password, BCrypt.gensalt()), image = user.image ?: currentUser.image,
                bio = user.bio ?: currentUser.bio)
        // update token only if email changed
        if (currentUser.email != u.email) {
            u.token = service.newToken(u)
        }

        return view(repository.save(u))
    }

    private fun checkUserAvailability(errors: BindException, email: String?, username: String?) {
        email?.let { email ->
            if (repository.existsByEmail(email)) {
                errors.addError(FieldError("", "email", "already taken"))
            }
        }
        username?.let { username ->
            if (repository.existsByUsername(username)) {
                errors.addError(FieldError("", "username", "already taken"))
            }
        }
    }

    fun view(user: User) = mapOf("user" to user)
}