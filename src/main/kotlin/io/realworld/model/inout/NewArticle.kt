package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@JsonRootName("article")
class NewArticle {
    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    var title: String? = ""

    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    var description: String? = ""

    @NotNull(message = "can't be missing")
    @Size(min = 1, message = "can't be empty")
    var body: String? = ""

    var tagList: List<String> = listOf()
}