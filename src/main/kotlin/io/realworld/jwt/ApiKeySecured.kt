package io.realworld.jwt

import java.lang.annotation.Inherited

@MustBeDocumented
@Inherited
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiKeySecured(val mandatory: Boolean = true)
