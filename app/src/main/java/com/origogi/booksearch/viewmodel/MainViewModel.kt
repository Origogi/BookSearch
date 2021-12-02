package com.origogi.booksearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.origogi.booksearch.TAG
import com.origogi.booksearch.model.Book
import com.origogi.booksearch.model.RetrofitService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _books: MutableLiveData<List<Book>> = MutableLiveData()
    val books: LiveData<List<Book>>
        get() = _books

    fun search(words: List<String>, excludeWord: String) {
        Log.d(TAG, "search $words $excludeWord")

        viewModelScope.launch {
            _books.value = emptyList()

            words.map {
                async {
                    RetrofitService.bookApi.getBooks(it, 1)
                }
            }.map {
                it.await()
            }.map {
                it.books
            }
            .map { books ->
                if (excludeWord.isNotBlank()) {
                    books.filter {
                        !it.title.lowercase().contains(excludeWord.lowercase())
                    }
                } else {
                    books
                }
            }.forEach { books ->
                _books.value = (_books.value ?: emptyList()) + books
            }
        }

    }
}