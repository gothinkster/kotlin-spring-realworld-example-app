package io.realworld.web

import io.realworld.exception.NotFoundException
import io.realworld.jwt.ApiKeySecured
import io.realworld.model.User
import io.realworld.model.inout.Profile
import io.realworld.repository.UserRepository
import io.realworld.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class ProfileHandler(val userRepository: UserRepository,
                     val userService: UserService) {

    @ApiKeySecured(mandatory = false)
    @GetMapping("/api/profiles/{username}")
    fun profile(@PathVariable username: String): Any {
        userRepository.findByUsername(username)?.let {
            return view(it, userService.currentUser())
        }
        throw NotFoundException()
    }

    @ApiKeySecured
    @PostMapping("/api/profiles/{username}/follow")
    fun follow(@PathVariable username: String): Any {
        userRepository.findByUsername(username)?.let {
            var currentUser = userService.currentUser()
            if (!currentUser.follows.contains(it)) {
                currentUser.follows.add(it)
                currentUser = userService.setCurrentUser(userRepository.save(currentUser))
            }
            return view(it, currentUser)
        }
        throw NotFoundException()
    }

    @ApiKeySecured
    @DeleteMapping("/api/profiles/{username}/follow")
    fun unfollow(@PathVariable username: String): Any {
        userRepository.findByUsername(username)?.let {
            var currentUser = userService.currentUser()
            if (currentUser.follows.contains(it)) {
                currentUser.follows.remove(it)
                currentUser = userService.setCurrentUser(userRepository.save(currentUser))
            }
            return view(it, currentUser)
        }
        throw NotFoundException()
    }

    fun view(user: User, currentUser: User) = mapOf("profile" to Profile.fromUser(user, currentUser))

}
