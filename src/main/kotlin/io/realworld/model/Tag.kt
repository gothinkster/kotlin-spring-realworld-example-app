package io.realworld.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Tag(var name: String = "",
               @Id @GeneratedValue(strategy = GenerationType.AUTO)
               var id: Long)