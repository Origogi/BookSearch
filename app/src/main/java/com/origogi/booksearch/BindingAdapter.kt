package com.origogi.booksearch

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
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
