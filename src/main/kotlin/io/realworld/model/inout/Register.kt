package io.realworld.model.inout

import org.hibernate.validator.constraints.NotEmpty

data class Login(@NotEmpty val email: String,
                 @NotEmpty val password: String)