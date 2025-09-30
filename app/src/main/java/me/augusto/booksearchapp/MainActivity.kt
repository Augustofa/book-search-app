package me.augusto.booksearchapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.augusto.booksearchapp.databinding.ActivityMainBinding
import okhttp3.OkHttpClient

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
                    searchBook(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    fun searchBook(query : String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvMessage.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val books = withContext(Dispatchers.IO) {
                    BookRepository.searchBooks(query)
                }

                if (books.isEmpty()) {
                    showResultMessage("No matching books found :c")
                } else {
                    bookAdapter.submitList(books)
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
            } catch(e: Exception) {
                Log.e("MAIN_ACTIVITY", "Error performing search", e)
                showResultMessage("Error: ${e.message}")
            }
        }
    }

    fun showResultMessage(message : String) {
        binding.progressBar.visibility = View.GONE
        binding.tvMessage.visibility = View.VISIBLE
        binding.tvMessage.text = message
    }
}
