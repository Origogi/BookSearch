package com.origogi.booksearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = RvAdapter {
                viewModel.loadMore()
            }
        }

        viewModel.errorMessage.observe(this, { msg ->
            if (msg.isNotEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }
}