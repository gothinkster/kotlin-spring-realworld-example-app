package io.realworld.model.inout

import com.fasterxml.jackson.annotation.JsonRootName
import io.realworld.model.User
import org.hibernate.validator.constraints.NotEmpty
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@JsonRootName("article")
data class NewArticle(@NotEmpty var title: String = "",
                      @NotEmpty var description: String = "",
                      @NotEmpty var body: String = "",
                      var tagList: List<String> = listOf())