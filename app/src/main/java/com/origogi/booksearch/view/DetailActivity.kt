package com.origogi.booksearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.origogi.booksearch.R
import com.origogi.booksearch.TAG
import com.origogi.booksearch.databinding.ActivityDetailBinding
import com.origogi.booksearch.databinding.ActivityMainBinding
import com.origogi.booksearch.viewmodel.DetailViewModel
import com.origogi.booksearch.viewmodel.MainViewModel

class DetailActivity : AppCompatActivity() {
    private val viewModel : DetailViewModel by viewModels()
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val isbn = intent.getStringExtra("isbn") ?: ""

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        viewModel.loadDataFromISBN(isbn)

    }
}