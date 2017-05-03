package io.realworld.repository.specification

import io.realworld.model.Article
import io.realworld.model.Tag
import io.realworld.model.User
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.Predicate

object ArticlesSpecifications {
    fun lastArticles(tag: Tag?, author: User?, fav: User?): Specification<Article> {
        return Specification { root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            tag?.let {
                val tagList = root.get<Collection<Tag>>("tagList")
                predicates.add(cb.isMember(tag, tagList))
            }

            author?.let {
                val user = root.get<String>("author")
                predicates.add(cb.equal(user, author))
            }

            fav?.let {
                val favorited = root.get<Collection<User>>("favorited")
                predicates.add(cb.isMember(fav, favorited))
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}
