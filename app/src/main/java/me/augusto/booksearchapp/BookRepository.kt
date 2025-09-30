package me.augusto.booksearchapp

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object BookRepository {

    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun searchBooks(query: String): List<Book> {
        val initialBooks = performSearch(query)

        coroutineScope {
            initialBooks.map { book ->
                async {
                    val rating = fetchRating(book)

                    book.averageRating = rating
                    book.isLoadingDetails = false
                }
            }.forEach { it.await() }
        }

        return initialBooks
    }

    private suspend fun performSearch(query: String): List<Book> {
        val formattedQuery = query.replace(" ", "%20")
        var url = "https://openlibrary.org/search.json?q=$formattedQuery&limit=10&lang=pt"
        var request = Request.Builder()
            .url(url)
            .build();

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw IOException("Error fetching books. Code: ${response.code}")
        }

        val responseBody = response.body?.string()
        if (responseBody != null) {
            return BookApiParser.parseBook(responseBody)
        } else {
            return emptyList()
        }
    }

    private suspend fun fetchRating(book: Book): Double? {
        var url = "https://openlibrary.org${book.workId}/ratings.json"
        var request = Request.Builder()
            .url(url)
            .build();

        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.w("REPOSITORY", "Failed to fetch rating for ${book.workId}. Code: ${response.code}")
                return null
            }

            val responseBody = response.body?.string()
            if (responseBody != null) {
                val ratingResponse = gson.fromJson(responseBody, RatingResponse::class.java)
                return ratingResponse.summary?.average
            }
        } catch (e: IOException) {
            Log.e("REPOSITORY", "Network error fetching rating for ${book.workId}", e)
        }
        return null
    }
}
