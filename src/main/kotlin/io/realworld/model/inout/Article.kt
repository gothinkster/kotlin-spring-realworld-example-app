package io.realworld.model.out

import io.realworld.model.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Article(val slug: String,
                   val title: String,
                   val  description: String,
                   val body: String,
                   val tagList: List<String>,
                   val createdAt: LocalDate,
                   val updatedAt: LocalDate?,
                   val author: Profile) {
    companion object {
        fun fromModel(model: io.realworld.model.Article, currentUser: User): Article {
            return Article(
                    slug=model.slug,
                    title=model.title,
                    description=model.description,
                    body=model.body,
                    tagList=model.tagList.map { it.name },
                    createdAt = model.createdAt,
                    updatedAt = model.updatedAt,
                    author = Profile.fromUser(model.author, currentUser)
                    )
        }
    }
}