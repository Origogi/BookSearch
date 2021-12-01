package com.origogi.booksearch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.origogi.booksearch.TAG

class MainViewModel : ViewModel() {

    fun search(words : String) {
        Log.d(TAG, "search $words")
    }
}