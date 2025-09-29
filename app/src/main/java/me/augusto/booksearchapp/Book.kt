package me.augusto.booksearchapp

data class Book(
    val title: String,
    val author: String?,
    val publicationYear: Int?,
    val pageCount: Int?,
    val coverUrl: String?
)
