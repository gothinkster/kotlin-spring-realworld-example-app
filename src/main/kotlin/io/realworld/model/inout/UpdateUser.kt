package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("user")
data class UpdateUser(var username: String? = null,
                      var email: String? = null,
                      var password: String? = null,
                      var image: String? = null,
                      var bio: String? = null)