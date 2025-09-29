package me.augusto.booksearchapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import me.augusto.booksearchapp.databinding.ActivityMainBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val bookAdapter = BookAdapter()
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.recyclerView.adapter = bookAdapter
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSearchView()
    }
    fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                query?.let {
                    makeRequest(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    fun makeRequest(query : String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvMessage.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE

        var url = "https://openlibrary.org/search.json?q=house%20of%20leaves&lang=pt&limit=5"
        var request = Request.Builder()
            .url(url)
            .build();

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API_REQUEST", "Failed to execute request", e)
                runOnUiThread {
                    showResultMessage("Failed to make request...")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.d("API_REQUEST", "Not successfull: ${it.code}")
                        runOnUiThread {
                            showResultMessage("Error: ${it.code}")
                        }
                        return
                    }

                    val responseBody = it.body?.string()
                    Log.d("API_REQUEST", "Response Body: $responseBody")

                    // TODO: Parse response into List<Book>

                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE

                        if (responseBody.isNullOrEmpty()) {
                            binding.tvMessage.visibility = View.VISIBLE
                            binding.tvMessage.text = "No matching books found."
                        } else {
                            // TODO: Parse the book list to update adapter
                            // bookAdapter.submitList(parsedBooks)
                            // binding.recyclerView.visibility = View.VISIBLE
                            binding.tvMessage.visibility = View.VISIBLE // For testing
                            binding.tvMessage.text = "Success! (Check Logcat for response)"
                        }
                    }
                }
            }
        })
    }

    fun showResultMessage(message : String) {
        binding.progressBar.visibility = View.GONE
        binding.tvMessage.visibility = View.VISIBLE
        binding.tvMessage.text = message
    }
}
