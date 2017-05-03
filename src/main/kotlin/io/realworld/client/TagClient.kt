package io.realworld.client

import feign.RequestLine
import io.realworld.model.Tag

interface TagClient {
    @RequestLine("GET /api/tags")
    fun tags(): List<Tag>
}
