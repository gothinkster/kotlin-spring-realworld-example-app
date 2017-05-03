package io.realworld.models

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Comment(var createdAt: Date = Date(),
                   var updatedAt: Date,
                   var body: String,
                   var author: User,
                   @Id @GeneratedValue(strategy = GenerationType.AUTO)
                   var id: Long)