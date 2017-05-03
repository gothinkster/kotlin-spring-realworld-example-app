package io.realworld.repository.specification

import io.realworld.model.Article
import io.realworld.model.User
import io.realworld.model.jpa.Article_
import io.realworld.model.Tag
import org.hibernate.criterion.Order
import org.springframework.data.jpa.domain.Specification

import javax.persistence.criteria.*

import au.com.console.jpaspecificationdsl.*

import java.util.ArrayList


object ArticlesSpecifications {

    fun lastArticles(tag: Tag?, author: String?, fav: User?): Specification<Article> {
        return Specification { root, query, cb ->
            val predicates = ArrayList<Predicate>()

            if (tag != null) {
                val tagList = root.get<Collection<Tag>>("tagList")
                predicates.add(cb.isMember(tag, tagList))
            }

            if (author != null) {
                val user = root.get<String>("author.username")
                predicates.add(cb.equal(user, author))
            }

            if (fav != null) {
                val favorited = root.get<Collection<User>>("favorited")
                predicates.add(cb.isMember(fav, favorited))
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}
