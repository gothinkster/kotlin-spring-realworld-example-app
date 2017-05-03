package io.realworld.model

import java.time.OffsetDateTime
import javax.persistence.*

@Entity
data class Article(var slug: String = "",
                   var title: String = "",
                   var description: String = "",
                   var body: String = "",
                   @ManyToMany
                   val tagList: MutableList<Tag> = mutableListOf(),
                   var createdAt: OffsetDateTime = OffsetDateTime.now(),
                   var updatedAt: OffsetDateTime = OffsetDateTime.now(),
                   @ManyToMany
                   var favorited: MutableList<User> = mutableListOf(),
                   @ManyToOne
                   var author: User = User(),
                   @Id @GeneratedValue(strategy = GenerationType.AUTO)
                   var id: Long = 0) {
    fun favoritesCount() = favorited.size
}