package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonRootName("user")
class Register {
    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    var username: String? = ""

    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    var email: String? = ""

    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    var password: String? = ""

    constructor(username: String?, email: String?, password: String?) {
        this.username = username
        this.email = email
        this.password = password
    }
}