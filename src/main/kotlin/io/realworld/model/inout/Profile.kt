package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import io.realworld.model.User

@JsonRootName("profile")
data class Profile(var username: String,
                   var bio: String,
                   var image: String?,
                   var following: Boolean) {
    companion object {
        fun fromUser(user: User, currentUser: User): Profile {
            return Profile(username = user.username, bio = user.bio, image = user.image,
                    following = currentUser.follows.contains(user))
        }
    }
}