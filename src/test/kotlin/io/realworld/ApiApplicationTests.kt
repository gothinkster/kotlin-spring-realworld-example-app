package io.realworld

import feign.Feign
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import io.realworld.client.ProfileClient
import io.realworld.client.TagClient
import io.realworld.client.UserClient
import io.realworld.client.response.InLogin
import io.realworld.client.response.InRegister
import io.realworld.model.inout.Login
import io.realworld.model.inout.Register
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTests {

    var randomServerPort: Int = 0
    @Autowired
    var environment: Environment? = null
    var tagClient: TagClient? = null
    var userClient: UserClient? = null
    var profileClient: ProfileClient? = null

    fun <T> buildClient(t: Class<T>): T {
        environment.let {
            randomServerPort = Integer.valueOf(it!!.getProperty("local.server.port"))
            return Feign.builder()
                    .encoder(GsonEncoder()).decoder(GsonDecoder())
                    .target(t, "http://localhost:${randomServerPort}")
        }
    }

    @BeforeEach
    fun before() {
        tagClient = buildClient(TagClient::class.java)
        userClient = buildClient(UserClient::class.java)
        profileClient = buildClient(ProfileClient::class.java)
    }

    @Test
    fun retrieveTags() {
        println("> tags: " + tagClient?.tags()?.tags)
    }

    @Test
    fun userAndProfileTest() {
        val fooRegister = userClient?.register(
                InRegister(Register(username = "foo", email = "foo@foo.com", password = "foo")))
        assertThat(fooRegister?.user?.username).isEqualTo("foo")
        assertThat(fooRegister?.user?.email).isEqualTo("foo@foo.com")
        assertThat(fooRegister?.user?.token).isNotNull()
        println("Register foo OK")

        val fooLogin = userClient?.login(InLogin(Login(email = "foo@foo.com", password = "foo")))
        assertThat(fooLogin?.user?.username).isEqualTo("foo")
        assertThat(fooLogin?.user?.email).isEqualTo("foo@foo.com")
        assertThat(fooLogin?.user?.token).isNotNull()
        println("Login foo OK")

        val barRegister = userClient?.register(
                InRegister(Register(username = "bar", email = "bar@bar.com", password = "bar")))
        assertThat(barRegister?.user?.username).isEqualTo("bar")
        assertThat(barRegister?.user?.email).isEqualTo("bar@bar.com")
        assertThat(barRegister?.user?.token).isNotNull()
        println("Register bar OK")

        val barLogin = userClient?.login(InLogin(Login(email = "bar@bar.com", password = "bar")))
        assertThat(barLogin?.user?.username).isEqualTo("bar")
        assertThat(barLogin?.user?.email).isEqualTo("bar@bar.com")
        assertThat(barLogin?.user?.token).isNotNull()
        println("Login bar OK")

        var profile = profileClient?.profile(barLogin?.user?.token!!, "foo")?.profile
        assertThat(profile?.username).isEqualTo("foo")
        assertThat(profile?.following!!).isFalse()
        println("Profile foo requested by bar OK")

        profile = profileClient?.follow(barLogin?.user?.token!!, "foo")?.profile
        assertThat(profile?.username).isEqualTo("foo")
        assertThat(profile?.following!!).isTrue()
        println("Foo is followed by bar OK")

        profile = profileClient?.unfollow(barLogin?.user?.token!!, "foo")?.profile
        assertThat(profile?.username).isEqualTo("foo")
        assertThat(profile?.following!!).isFalse()
        println("Foo is unfollowed by bar OK")
    }
}
