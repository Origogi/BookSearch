package com.origogi.booksearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.origogi.booksearch.R
import com.origogi.booksearch.TAG

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val isbn = intent.getStringExtra("isbn")
        Log.d(TAG, "isbn : ${isbn!!}")
    }
}