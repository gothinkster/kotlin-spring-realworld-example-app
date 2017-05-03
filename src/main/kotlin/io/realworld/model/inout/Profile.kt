package io.realworld.model.out

import io.realworld.model.User

data class Profile(var username: String,
                var bio: String,
                var image: String?,
                var following: Boolean) {
    companion object {
        fun fromUser(user: User, currentUser: User) : Profile {
            return Profile(username=user.username, bio=user.bio, image=user.image,
                    following=user.follows.contains(currentUser))
        }
    }
}