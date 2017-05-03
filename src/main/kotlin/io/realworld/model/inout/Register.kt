package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import org.hibernate.validator.constraints.NotEmpty

@JsonRootName("user")
data class Register(@NotEmpty var username: String = "",
                    @NotEmpty var email: String = "",
                    @NotEmpty var password: String = "")