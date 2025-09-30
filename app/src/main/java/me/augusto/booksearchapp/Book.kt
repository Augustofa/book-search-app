package me.augusto.booksearchapp

data class Book(
    val title: String,
    val author: String?,
    val publicationYear: Int?,
    val coverUrl: String?,
    val workId: String,

    // Async variables
    var pageCount: Int? = null,
    var averageRating: Double? = null,
    var isLoadingDetails: Boolean = true
)
