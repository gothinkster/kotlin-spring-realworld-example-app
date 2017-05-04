package io.realworld.client

import feign.Headers
import feign.RequestLine
import io.realworld.client.response.OutTag

interface TagClient {
    @RequestLine("GET /api/tags")
    @Headers("Content-Type: application/json")
    fun tags(): OutTag
}
