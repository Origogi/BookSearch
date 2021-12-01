package com.origogi.booksearch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.origogi.booksearch.TAG
import com.origogi.booksearch.model.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    fun search(words: String) {
        Log.d(TAG, "search $words")

        viewModelScope.launch {
            val response =
                RetrofitService.bookApi.getBooks(words, 1)

            Log.d(TAG, "result $response")

        }

    }
}