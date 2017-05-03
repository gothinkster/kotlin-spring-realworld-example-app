package io.realworld.jwt

import io.realworld.model.User
import io.realworld.repository.UserRepository
import io.realworld.service.UserService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.ResponseStatus

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.lang.reflect.Method

/**
 * Aspect whose goal is to check automatically that methods
 * having a @ApiKeySecured annotation are correctly accessed
 * by users having a valid API Key (JWT).

 * A check against the user service is done to find the user
 * having the api key passed as request header/parameter.

 * If the API Key is valid the annotated method is executed,
 * otherwise the response is set with an UNAUTHORIZED status and the annotated
 * method is not executed.
 */
@Aspect
@Component
class ApiKeySecuredAspect {
    @Autowired
    private val userService: UserService? = null
    @Autowired
    private val request: HttpServletRequest? = null

    @Pointcut(value = "execution(@io.realworld.jwt.ApiKeySecured * *.*(..))")
    fun securedApiPointcut() {
    }

    @Around("securedApiPointcut()")
    @Throws(Throwable::class)
    fun aroundSecuredApiPointcut(joinPoint: ProceedingJoinPoint): Any? {
        if (request == null) {
            throw IllegalStateException("No HttpServletRequest could be found.")
        }

        // see the ExposeResponseInterceptor class.
        val response = request.getAttribute(ExposeResponseInterceptor.KEY) as HttpServletResponse ?: throw IllegalStateException("No HttpServletResponse could be found. There's a problem with ExposeResponseInterceptor.")

        // check for Bearer JWT Authentication
        val apiKey = request.getHeader("Authorization").replace("Token |Bearer ", "")

        if (StringUtils.isEmpty(apiKey)) {
            if (LOG.isInfoEnabled)
                LOG.info("No X-API-Key part of the request header/parameters, returning {}.", HttpServletResponse.SC_UNAUTHORIZED)

            setStatus(response, HttpServletResponse.SC_UNAUTHORIZED)
            response.setHeader("X-API-Key", "You shall not pass without providing an API Key")
            response.writer.write("{\"error\": \"You must provide an X-API-Key header.\"}")
            response.writer.flush()

            return null
        }

        // find the user associated to the given api key.
        val user = userService!!.findByToken(apiKey)
        if (user == null) {
            if (LOG.isInfoEnabled)
                LOG.info("No user with X-API-Key: {}, returning {}.", apiKey, HttpServletResponse.SC_UNAUTHORIZED)

            setStatus(response, HttpServletResponse.SC_UNAUTHORIZED)
            response.setHeader("X-API-Key", "You shall not pass without providing a valid API Key")
            response.writer.write("{\"error\": \"You must provide a valid X-API-Key header.\"}")
            response.writer.flush()

            return null
        }

        // check for needed roles
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val anno = method.getAnnotation(ApiKeySecured::class.java)
        userService.setCurrentUser(user)


        if (LOG.isInfoEnabled)
            LOG.info("OK accessing resource, proceeding.")

        // execute
        try {
            val result = joinPoint.proceed()
            // remove user from thread local
            userService.clearCurrentUser()

            if (LOG.isInfoEnabled)
                LOG.info("DONE accessing resource.")

            return result
        } catch (e: Throwable) {
            // check for custom exception
            val rs = e.javaClass.getAnnotation(ResponseStatus::class.java)
            if (rs != null) {
                LOG.error("ERROR accessing resource, reason: '{}', status: {}.",
                        if (StringUtils.isEmpty(e.message)) rs.reason() else e.message,
                        rs.value())
            } else {
                LOG.error("ERROR accessing resource", e)
            }
            throw e
        }

    }

    fun setStatus(response: HttpServletResponse?, sc: Int) {
        if (response != null)
            response.status = sc
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ApiKeySecuredAspect::class.java)
    }
}