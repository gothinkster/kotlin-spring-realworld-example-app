package io.realworld.repository

import io.realworld.model.Tag
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : ReactiveCrudRepository<Tag, Long> {
}