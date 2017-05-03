package io.realworld.client

import feign.RequestLine
import io.realworld.model.out.Profile
import org.springframework.web.bind.annotation.PathVariable

interface ProfileClient {
    @RequestLine("GET /api/profiles/{username}")
    fun profile(@PathVariable username: String) : Profile

    @RequestLine("POST /api/profiles/{username}/follow")
    fun follow(@PathVariable username: String) : Profile

    @RequestLine("DELETE /api/profiles/{username}/follow")
    fun unfollow(@PathVariable username: String) : Profile
}
