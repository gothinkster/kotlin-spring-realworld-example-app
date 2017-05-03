package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import org.hibernate.validator.constraints.NotEmpty

@JsonRootName("user")
data class Login(@NotEmpty var email: String = "",
                 @NotEmpty var password: String = "")