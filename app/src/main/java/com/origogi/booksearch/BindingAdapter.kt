package com.origogi.booksearch

import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.origogi.booksearch.model.Book
import com.origogi.booksearch.view.RvAdapter
import com.origogi.booksearch.viewmodel.MainViewModel
import java.lang.Exception
import java.lang.IllegalArgumentException

@BindingAdapter("setOnQueryTextListener")
fun setOnQueryTextListener(view: SearchView, viewModel: MainViewModel) {

    view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query.isNullOrEmpty()) {
                Toast.makeText(view.context, "input keyword", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val (includeWords, excludeWord) =  parseIncludeAndExcludeWords(query)
                    viewModel.search(includeWords, excludeWord)
                } catch (e : Exception) {
                    Log.e(TAG, e.toString())
                    Toast.makeText(view.context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
            view.clearFocus()

            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    })
}

fun parseIncludeAndExcludeWords(query : String) : Pair<List<String>, String>  {
    val includeOP = "|"
    val excludeOP = "-"

    if (includeOP in query && excludeOP in query) {
        throw IllegalArgumentException("too many operator")
    }

    when {
        includeOP in query -> {
            val tokens = query.split(includeOP)

            if (tokens.size > 2) {
                throw IllegalArgumentException("invalid keyword")
            }
            return tokens to ""
        }
        excludeOP in query -> {
            val tokens = query.split(excludeOP)

            if (tokens.size > 2) {
                throw IllegalArgumentException("invalid keyword")
            }
            return listOf(tokens[0]) to tokens[1]
        }
        else -> {
            return listOf(query) to ""
        }
    }


}

@BindingAdapter("bindItem")
fun bindItem(recyclerView: RecyclerView, items: LiveData<List<Book>>) {
    val adapter = recyclerView.adapter
    if (adapter is RvAdapter) {
        items.value?.let {
            adapter.updateBooks(it)
        }
    }
}

@BindingAdapter("bindState")
fun bindState(recyclerView: RecyclerView, state: LiveData<State>) {
    val adapter = recyclerView.adapter
    if (adapter is RvAdapter) {
        state.value?.let {
            adapter.updateState(it)
        }
    }
}

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}


