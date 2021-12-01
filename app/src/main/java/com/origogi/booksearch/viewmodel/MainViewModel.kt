package com.origogi.booksearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.origogi.booksearch.TAG
import com.origogi.booksearch.model.Book
import com.origogi.booksearch.model.RetrofitService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _books : MutableLiveData<List<Book>> = MutableLiveData()
    val books : LiveData<List<Book>>
        get() = _books

    fun search(words: String) {
        Log.d(TAG, "search $words")

        viewModelScope.launch {
            _books.value = emptyList()

            val response =
                RetrofitService.bookApi.getBooks(words, 1)
            Log.d(TAG, "result $response")

            _books.value = response.books
        }

    }
}