package io.realworld.model.out

import io.realworld.model.User
import java.time.LocalDate

data class Comment(val createdAt: LocalDate,
                   val updatedAt: LocalDate?,
                   val body: String,
                   val author: Profile,
                   val id: Long) {
    companion object {
        fun fromModel(model: io.realworld.model.Comment, currentUser: User): Comment {
            return Comment(
                    id=model.id,
                    body=model.body,
                    createdAt = model.createdAt,
                    updatedAt = model.updatedAt,
                    author = Profile.fromUser(model.author, currentUser)
            )
        }
    }
}