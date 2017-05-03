package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import org.hibernate.validator.constraints.NotEmpty

@JsonRootName("article")
data class NewArticle(@NotEmpty var title: String = "",
                      @NotEmpty var description: String = "",
                      @NotEmpty var body: String = "",
                      var tagList: List<String> = listOf())