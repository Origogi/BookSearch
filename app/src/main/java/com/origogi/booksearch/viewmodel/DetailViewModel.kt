package com.origogi.booksearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.origogi.booksearch.State
import com.origogi.booksearch.TAG
import com.origogi.booksearch.model.BookDetail
import com.origogi.booksearch.model.RetrofitService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _bookDetail = MutableLiveData<BookDetail>()
    val bookDetail: LiveData<BookDetail>
        get() = _bookDetail

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e(TAG, "$exception")

        viewModelScope.launch {
            _errorMessage.value = exception.message
        }
    }

    fun loadDataFromISBN(isbn: String) {
        Log.d(TAG, "isbn : $isbn")

        viewModelScope.launch(handler) {
            val response = RetrofitService.bookApi.getBook(isbn)
            Log.d(TAG, "response : $response")

            _bookDetail.value = BookDetail(
                response.title,
                response.subtitle,
                response.authors,
                response.publisher,
                response.language,
                response.isbn10,
                response.isbn13,
                response.pages,
                response.year,
                response.rating,
                response.desc,
                response.price,
                response.image,
                response.url
            )
        }
    }
}