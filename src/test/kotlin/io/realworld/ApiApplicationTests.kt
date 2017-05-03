package io.realworld

import feign.Feign
import feign.Logger
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import io.realworld.client.TagClient
import io.realworld.client.UserClient
import io.realworld.model.inout.Login
import io.realworld.model.inout.Register
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.core.env.Environment


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTests {

    var randomServerPort: Int = 0
    @Autowired
    var environment: Environment? = null
    var tagClient : TagClient? = null
    var userClient : UserClient? = null

    @Before
    fun before() {
         environment.let {
             randomServerPort = Integer.valueOf(it!!.getProperty("local.server.port"))
             tagClient = Feign.builder()
                     .encoder(JacksonEncoder()).decoder(JacksonDecoder())
                     .target(TagClient::class.java, "http://localhost:$randomServerPort")
             userClient = Feign.builder()
                     .encoder(JacksonEncoder()).decoder(JacksonDecoder())
                     .logger(Logger.JavaLogger())
                     .logLevel(Logger.Level.FULL)
                     .target(UserClient::class.java, "http://localhost:$randomServerPort")
         }
    }

	@Test
	fun retrieveTags() {
		println("> tags: " + tagClient?.let {
            it.tags()
        })
	}

    @Test
    fun registerAndLogin() {
        println("> register: " + userClient?.let {
            it.register(Register(username="Foo", email="foo@bar.com", password="LOL"))
        })

        println("> login: " + userClient?.let {
            it.login(Login(email="foo@bar.com", password="LOL"))
        })
    }

}
