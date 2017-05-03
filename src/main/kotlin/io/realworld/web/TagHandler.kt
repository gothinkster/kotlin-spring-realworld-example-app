package io.realworld.web

import io.realworld.model.Tag
import io.realworld.repository.TagRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TagHandler(val repository: TagRepository) {
    @GetMapping("/api/tags")
    fun allTags() = mapOf("tags" to repository.findAll().map(Tag::name))
}