package io.realworld.web

import io.realworld.repository.TagRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body

@RestController
class TagHandler(val repository: TagRepository) {
    @GetMapping("/api/tags")
    fun allTags() = ok().body(repository.findAll())
}