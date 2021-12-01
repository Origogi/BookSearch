package com.origogi.booksearch

import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.origogi.booksearch.model.Book
import com.origogi.booksearch.view.RvAdapter
import com.origogi.booksearch.viewmodel.MainViewModel

@BindingAdapter("setOnQueryTextListener")
fun setOnQueryTextListener(view: SearchView, viewModel: MainViewModel) {

    view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query.isNullOrEmpty()) {
                // TODO handle that query is empty
            } else {
                viewModel.search(query)
            }
            view.clearFocus()

            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    })
}

@BindingAdapter("bindItem")
fun bindItem(recyclerView: RecyclerView, items: LiveData<List<Book>>) {
    val adapter = recyclerView.adapter
    if (adapter is RvAdapter) {
        items.value?.let {
            adapter.update(it)
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


