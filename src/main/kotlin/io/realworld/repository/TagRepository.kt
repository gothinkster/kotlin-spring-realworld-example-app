package io.realworld.repository

import io.realworld.model.Tag
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "tag", path = "/api/tags")
interface TagRepository : PagingAndSortingRepository<Tag, Long> {
}