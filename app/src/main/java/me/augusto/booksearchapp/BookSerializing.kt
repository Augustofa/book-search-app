package me.augusto.booksearchapp

import com.google.gson.annotations.SerializedName

data class OpenLibraryResponse(
    @SerializedName("docs")
    val docs: List<BookDocument>
)

data class BookDocument(
    @SerializedName("title")
    val title: String,

    @SerializedName("author_name")
    val authorName: List<String>?,

    @SerializedName("publish_info")
    val publishInfo: PublishInfo?,

    @SerializedName("cover_i")
    val coverId: Int?
)

data class PublishInfo(
    @SerializedName("publisher")
    val publisher: String?,

    @SerializedName("publish_year")
    val publishYear: Int?,
)

data class Location(
    @SerializedName("country")
    val country: String?
)