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

    @SerializedName("first_publish_year")
    val publishYear: Int?,

    @SerializedName("publish_info")
    val publishInfo: PublishInfo?,

    @SerializedName("cover_i")
    val coverId: Int?,

    @SerializedName("key")
    val workId: String
)

data class PublishInfo(
    @SerializedName("publisher")
    val publisher: String?,

    @SerializedName("location")
    val location: Location?
)

data class Location(
    @SerializedName("country")
    val country: String?
)

// --- Rating Request ---

data class RatingResponse(
    @SerializedName("summary")
    val summary: RatingValue?
)

data class RatingValue(
    @SerializedName("average")
    val average: Double?
)