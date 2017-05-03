package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import io.realworld.model.User
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@JsonRootName("article")
data class Article(var title: String? = null,
                   var description: String? = null,
                   var body: String? = null,
                   var tagList: List<String> = listOf(),
                   var slug: String = "",
                   var createdAt: String = "",
                   var updatedAt: String = "",
                   var author: Profile = Profile(username = "", bio = "", image = "", following = false),
                   var favorited: Boolean = false,
                   var favoritesCount: Int = 0) {
    companion object {
        fun dateFormat(date: OffsetDateTime): String {
            return date.toZonedDateTime().withZoneSameInstant(ZoneId.of("Z")).format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
        }

        fun fromModel(model: io.realworld.model.Article, currentUser: User): Article {
            return Article(
                    slug = model.slug,
                    title = model.title,
                    description = model.description,
                    body = model.body,
                    tagList = model.tagList.map { it.name },
                    createdAt = dateFormat(model.createdAt),
                    updatedAt = dateFormat(model.updatedAt),
                    author = Profile.fromUser(model.author, currentUser),
                    favorited = model.favorited.contains(currentUser),
                    favoritesCount = model.favorited.size)
        }
    }
}