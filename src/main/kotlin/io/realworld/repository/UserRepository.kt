package io.realworld.repository

import io.realworld.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean
    fun findByEmail(email: String): User?
    fun findByToken(token: String): User?
    fun findByEmailAndPassword(email: String, password: String): User?
    fun findByUsername(username: String): User?
}