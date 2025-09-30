package me.augusto.booksearchapp

import com.google.gson.Gson

object BookApiParser {

    fun parseBook(jsonString: String): List<Book> {
        val gson = Gson()
        val apiResponse = gson.fromJson(jsonString, OpenLibraryResponse::class.java)

        return apiResponse.docs
            .take(10)
            .map { doc ->
                Book(
                    workId = doc.workId,
                    title = doc.title,
                    author = doc.authorName?.firstOrNull(),
                    publicationYear = doc.publishYear,
                    coverUrl = doc.coverId?.let {
                        "https://covers.openlibrary.org/b/id/$it-L.jpg"
                    }
                )
            }
    }
}

