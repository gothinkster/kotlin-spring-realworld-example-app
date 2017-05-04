package io.realworld.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import javax.persistence.*

@Entity
@JsonRootName("user")
data class User(var email: String = "",
                @JsonIgnore
                var password: String = "",
                var token: String = "",
                var username: String = "",
                var bio: String = "",
                var image: String = "",
                @ManyToMany
                @JsonIgnore
                var follows: MutableList<User> = mutableListOf(),
                @Id @GeneratedValue(strategy = GenerationType.AUTO)
                var id: Long = 0) {
    override fun toString(): String = "User($email, $username)"
}