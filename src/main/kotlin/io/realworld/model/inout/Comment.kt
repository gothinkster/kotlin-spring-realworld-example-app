package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import io.realworld.model.User
import java.time.format.DateTimeFormatter

@JsonRootName("comment")
data class Comment(val createdAt: String,
                   val updatedAt: String?,
                   val body: String,
                   val author: Profile,
                   val id: Long) {
    companion object {
        fun fromModel(model: io.realworld.model.Comment, currentUser: User): Comment {
            return Comment(
                    id = model.id,
                    body = model.body,
                    createdAt = model.createdAt.format(DateTimeFormatter.ISO_DATE_TIME),
                    updatedAt = model.updatedAt?.format(DateTimeFormatter.ISO_DATE_TIME),
                    author = Profile.fromUser(model.author, currentUser)
            )
        }
    }
}