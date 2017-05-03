package io.realworld.web

import io.realworld.repository.TagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TagHandler(@Autowired val repository: TagRepository) {
    @GetMapping("/api/tags")
    fun allTags() = repository.findAll()
}