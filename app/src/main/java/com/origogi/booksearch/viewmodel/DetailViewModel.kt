package com.origogi.booksearch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.origogi.booksearch.TAG

class DetailViewModel : ViewModel() {

    fun loadDataFromISBN(isbn : String) {
        Log.d(TAG, "isbn : $isbn")
    }
}