package com.origogi.booksearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.origogi.booksearch.R
import com.origogi.booksearch.databinding.ActivityMainBinding
import com.origogi.booksearch.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
    }
}