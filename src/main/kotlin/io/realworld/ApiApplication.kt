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
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
@EnableCaching
@SpringBootApplication
class ApiApplication : WebMvcConfigurerAdapter() {
    override fun addInterceptors(registry: InterceptorRegistry?) {
        registry!!.addInterceptor(exposeResponseInterceptor())
    }

    @Bean
    fun exposeResponseInterceptor(): ExposeResponseInterceptor {
        return ExposeResponseInterceptor()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ApiApplication::class.java, *args)
}


