package me.augusto.booksearchapp

data class Book(
    val title: String,
    val author: String?,
    val publicationYear: Int?,
    val country: String?,
    val coverUrl: String?
)
