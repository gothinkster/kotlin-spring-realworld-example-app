package io.realworld

import io.realworld.jwt.ExposeResponseInterceptor
import io.realworld.model.Article
import io.realworld.model.Tag
import io.realworld.model.User
import io.realworld.repository.ArticleRepository
import io.realworld.repository.TagRepository
import io.realworld.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter


@Configuration
@EnableCaching
@SpringBootApplication
class ApiApplication(val tagRepository: TagRepository,
                     val userRepository: UserRepository,
                     val articleRepository: ArticleRepository) : WebMvcConfigurerAdapter(), CommandLineRunner {

    override fun addInterceptors(registry: InterceptorRegistry?) {
        registry!!.addInterceptor(exposeResponseInterceptor())
    }

    @Bean
    fun exposeResponseInterceptor() = ExposeResponseInterceptor()

    @Bean
    fun methodValidationPostProcessor(): MethodValidationPostProcessor {
        val mv = MethodValidationPostProcessor()
        mv.setValidator(validator())
        return mv
    }

    @Bean
    fun validator() = LocalValidatorFactoryBean()

    override fun run(vararg p0: String?) {
        val java = tagRepository.save(Tag(name = "java"))
        val kotlin = tagRepository.save(Tag(name = "kotlin"))
        val dev = tagRepository.save(Tag(name = "dev"))
        val clojure = tagRepository.save(Tag(name = "clojure"))

        var alex = userRepository.save(User(email = "a.grison@gmail.com",
                password = BCrypt.hashpw("foofoo", BCrypt.gensalt()),
                token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTM4MzE2NzEsInN1YiI6ImEuZ3Jpc29uQGdtYWlsLmNvbSIsImlzcyI6IktvdGxpbiZTcHJpbmciLCJleHAiOjE0OTQ2OTU2NzF9.nxRpiUsRgNGgJPhMws0GiidqihaQcTBv1MxPP7LNAKw",
                username = "Alex", bio = "This is Alex"))
        val rich = userRepository.save(User(email = "rich@gmail.com",
                password = BCrypt.hashpw("barbar", BCrypt.gensalt()),
                token = "lmao",
                username = "Rich", bio = "This is Rich", follows = mutableListOf(alex)))
        alex.follows.add(rich)
        alex = userRepository.save(alex)

        articleRepository.save(Article(title = "Kotlin is fun", body = "Kotlin is somewhat fun", slug = "kotlin-is-fun",
                author = alex, tagList = mutableListOf(dev, kotlin)))
        articleRepository.save(Article(title = "Java is still cool", body = "Java is still cool", slug = "java-is-still-cool",
                author = alex, tagList = mutableListOf(dev, java)))
        articleRepository.save(Article(title = "Clojure is the future", body = "Clojure up!", slug = "clojure-is-the-future",
                author = alex, tagList = mutableListOf(dev, clojure),
                favorited = mutableListOf(rich)))
        articleRepository.save(Article(title = "Clojure spec", body = "Clojure spec!", slug = "clojure-spec",
                author = rich, tagList = mutableListOf(dev, clojure),
                favorited = mutableListOf(alex)))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ApiApplication::class.java, *args)
}



