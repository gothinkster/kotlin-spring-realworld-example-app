package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@JsonRootName("user")
class Login(
    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$",
        message = "must be a valid email"
    ) var email: String?, @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty") var password: String?
)
