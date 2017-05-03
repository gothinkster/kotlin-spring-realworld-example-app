package io.realworld.models

import java.time.LocalDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Article(var slug: String = "",
                   var title: String = "",
                   var description: String = "",
                   var body: String = "",
                   var tagList: List<Tag>,
                   var createdAt: LocalDate,
                   var updatedAt: LocalDate,
                   var favorited: Boolean,
                   var favoritesCount: Int,
                   var author: User,
                   @Id @GeneratedValue(strategy = GenerationType.AUTO)
                   var id: Long)