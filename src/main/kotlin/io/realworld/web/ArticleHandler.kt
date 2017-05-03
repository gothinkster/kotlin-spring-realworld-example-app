package io.realworld.web

import io.realworld.exception.ForbiddenRequestException
import io.realworld.exception.NotFoundException
import io.realworld.jwt.ApiKeySecured
import io.realworld.model.Article
import io.realworld.model.Comment
import io.realworld.model.Tag
import io.realworld.model.User
import io.realworld.model.inout.NewArticle
import io.realworld.model.inout.UpdateArticle
import io.realworld.repository.ArticleRepository
import io.realworld.repository.CommentRepository
import io.realworld.repository.TagRepository
import io.realworld.repository.UserRepository
import io.realworld.repository.specification.ArticlesSpecifications
import io.realworld.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import java.time.OffsetDateTime
import io.realworld.model.inout.Article as ArticleIO
import io.realworld.model.inout.Comment as CommentOut

@RestController
class ArticleHandler(val repository: ArticleRepository,
                     val userService: UserService,
                     val userRepository: UserRepository,
                     val commentRepository: CommentRepository,
                     val tagRepository: TagRepository) {

    @ApiKeySecured(mandatory = false)
    @GetMapping("/api/articles")
    fun articles(@RequestParam(defaultValue = "20") limit: Int,
                 @RequestParam(defaultValue = "0") offset: Int,
                 @RequestParam(defaultValue = "") tag: String,
                 @RequestParam(defaultValue = "") author: String,
                 @RequestParam(defaultValue = "") favorited: String): Any {
        val p = PageRequest.of(offset, limit, Sort.Direction.DESC, "createdAt")

        val articles = repository.findAll(ArticlesSpecifications.lastArticles(
                if (tag != "") tagRepository.findByName(tag) else null,
                if (author != "") userRepository.findByUsername(author) else null,
                if (favorited != "") userRepository.findByUsername(favorited) else null
        ), p).toList()

        return articlesView(articles, userService.currentUser())
    }

    @ApiKeySecured
    @GetMapping("/api/articles/feed")
    fun feed(@RequestParam(defaultValue = "20") limit: Int,
             @RequestParam(defaultValue = "0") offset: Int): Any {
        val currentUser = userService.currentUser()
        val articles = repository.findByAuthorIdInOrderByCreatedAtDesc(currentUser.follows.map { it.id },
                PageRequest.of(offset, limit))
        return articlesView(articles, currentUser)
    }

    @ApiKeySecured(mandatory = false)
    @GetMapping("/api/articles/{slug}")
    fun article(@PathVariable slug: String): Any {
        repository.findBySlug(slug)?.let {
            return articleView(it, userService.currentUser())
        }
        throw NotFoundException()
    }

    @ApiKeySecured
    @PostMapping("/api/articles")
    fun newArticle(@RequestBody newArticle: NewArticle): Any {
        val currentUser = userService.currentUser()
        if (newArticle.title == "" || newArticle.description == "" || newArticle.body == "")
            throw TODO()

        // search for tags
        val tagList = newArticle.tagList.map {
            tagRepository.findByName(it) ?: tagRepository.save(Tag(name = it))
        }

        val article = Article(slug = newArticle.title.toLowerCase().replace(" ", "-"),
                author = currentUser, title = newArticle.title, description = newArticle.description,
                body = newArticle.body, tagList = tagList.toMutableList())

        return articleView(repository.save(article), currentUser)

    }

    @ApiKeySecured
    @PutMapping("/api/articles/{slug}")
    fun updateArticle(@PathVariable slug: String, @RequestBody article: UpdateArticle): Any {
        repository.findBySlug(slug)?.let {
            val currentUser = userService.currentUser()
            if (it.author.id != currentUser.id)
                throw ForbiddenRequestException()

            // search for tags
            val tagList = article.tagList?.map {
                tagRepository.findByName(it) ?: tagRepository.save(Tag(name = it))
            }

            val updated = it.copy(title = article.title ?: it.title,
                    description = article.description ?: it.description,
                    body = article.body ?: it.body,
                    slug = (article.title ?: it.title).toLowerCase().replace(" ", "-"),
                    updatedAt = OffsetDateTime.now(),
                    tagList = if (tagList == null || tagList.isEmpty()) it.tagList
                    else tagList.toMutableList())

            return articleView(repository.save(updated), currentUser)
        }
        throw NotFoundException()
    }

    @ApiKeySecured
    @DeleteMapping("/api/articles/{slug}")
    fun deleteArticle(@PathVariable slug: String) {
        repository.findBySlug(slug)?.let {
            if (it.author.id != userService.currentUser().id)
                throw ForbiddenRequestException()

            return repository.delete(it)
        }
        throw NotFoundException()
    }

    @ApiKeySecured(mandatory = false)
    @GetMapping("/api/articles/{slug}/comments")
    fun articleComments(@PathVariable slug: String): Any {
        repository.findBySlug(slug)?.let {
            val currentUser = userService.currentUser()
            return commentsView(commentRepository.findByArticle(it), currentUser)
        }
        throw NotFoundException()
    }

    @ApiKeySecured
    @PostMapping("/api/articles/{slug}/comments")
    fun addComment(@PathVariable slug: String, @RequestBody comment: CommentOut): Any {
        repository.findBySlug(slug)?.let {
            val currentUser = userService.currentUser()
            val newComment = Comment(body = comment.body, article = it, author = currentUser)
            return commentView(commentRepository.save(newComment), currentUser)
        }
        throw NotFoundException()
    }

    @ApiKeySecured
    @DeleteMapping("/api/articles/{slug}/comments/{id}")
    fun deleteComment(@PathVariable slug: String, @PathVariable id: Long) {
        repository.findBySlug(slug)?.let {
            val comment = commentRepository.findOne(id).orElseThrow({ NotFoundException() })
            if (comment.article.id == it.id)
                return commentRepository.delete(comment)
            throw ForbiddenRequestException()
        }
        throw NotFoundException()
    }

    @ApiKeySecured
    @PostMapping("/api/articles/{slug}/favorite")
    fun favoriteArticle(@PathVariable slug: String): Any {
        repository.findBySlug(slug)?.let {
            val currentUser = userService.currentUser()
            if (!it.favorited.contains(currentUser)) {
                it.favorited.add(currentUser)
                return articleView(repository.save(it), currentUser)
            }
            return articleView(it, currentUser)
        }
        throw NotFoundException()
    }

    @ApiKeySecured
    @DeleteMapping("/api/articles/{slug}/favorite")
    fun unfavoriteArticle(@PathVariable slug: String): Any {
        repository.findBySlug(slug)?.let {
            val currentUser = userService.currentUser()
            if (it.favorited.contains(currentUser)) {
                it.favorited.remove(currentUser)
                return articleView(repository.save(it), currentUser)
            }
            return articleView(it, currentUser)
        }
        throw NotFoundException()
    }

    // helpers

    fun articleView(article: Article, currentUser: User)
            = mapOf("article" to ArticleIO.fromModel(article, currentUser))

    fun articlesView(articles: List<Article>, currentUser: User)
            = mapOf("articles" to articles.map { ArticleIO.fromModel(it, userService.currentUser()) },
            "articlesCount" to articles.size)

    fun commentView(comment: Comment, currentUser: User)
            = mapOf("comment" to CommentOut.fromModel(comment, currentUser))

    fun commentsView(comments: List<Comment>, currentUser: User)
            = mapOf("comments" to comments.map { CommentOut.fromModel(it, currentUser) })
}