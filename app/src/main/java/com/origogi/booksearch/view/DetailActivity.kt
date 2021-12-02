package com.origogi.booksearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.databinding.DataBindingUtil
import com.origogi.booksearch.R
import com.origogi.booksearch.TAG
import com.origogi.booksearch.databinding.ActivityDetailBinding
import com.origogi.booksearch.databinding.ActivityMainBinding
import com.origogi.booksearch.viewmodel.DetailViewModel
import com.origogi.booksearch.viewmodel.MainViewModel

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DetailScreen()
        }
    }
}

@Composable
fun DetailScreen() {
    MaterialTheme {
        Text(text = "compose screen")
    }
}