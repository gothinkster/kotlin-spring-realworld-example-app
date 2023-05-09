package io.realworld.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Tag(val name: String = "",
               @Id @GeneratedValue(strategy = GenerationType.AUTO)
               @JsonIgnore
               var id: Long = 0)