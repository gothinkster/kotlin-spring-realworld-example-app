package io.realworld.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class User(var email: String,
                var token: String,
                var username: String,
                var bio: String,
                var image: String?,
                var following: Boolean,
                @Id @GeneratedValue(strategy = GenerationType.AUTO)
                var id: Long)