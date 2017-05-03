/**************************************************
 * Project: Da Club - http://daclub.sport
 * © dageeks - http://dageeks.com

 * Alexandre Grison & Antoine Picone
 */
package io.realworld.jwt

import com.dageeks.daclub.core.domain.Role

import java.lang.annotation.*

@Documented
@Inherited
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
annotation class ApiKeySecured(val roles: Array<Role> = arrayOf())
