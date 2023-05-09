package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@JsonRootName("comment")
class NewComment {
    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    var body: String? = ""
}