package me.augusto.booksearchapp

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.recyclerView.adapter = bookAdapter
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    makeRequest(it)
                }
                return true
            }
        })
    }

    fun makeRequest(query : String) {
        var url = "https://openlibrary.org/search.json?q=house%20of%20leaves&lang=por"
        var client = OkHttpClient();
        var request = Request.Builder()
            .url(url)
            .build();

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API_REQUEST", "Failed to execute request", e)
                runOnUiThread {
                    // You can update the UI here, but you must use runOnUiThread
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        runOnUiThread {
                        }
                        return
                    }

                    val responseBody = it.body?.string()
                    Log.d("API_REQUEST", "Response Body: $responseBody")

                    runOnUiThread {
                        // To update the UI, you must switch back to the main thread
                    }
                }
            }
        })
    }
}
