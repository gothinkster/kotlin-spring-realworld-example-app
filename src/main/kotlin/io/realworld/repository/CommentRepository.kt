package io.realworld.repository

import io.realworld.model.Article
import io.realworld.model.Comment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : CrudRepository<Comment, Long> {
    fun findByArticle(article: Article): List<Comment>
    fun findByArticleOrderByCreatedAtDesc(article: Article): List<Comment>
}