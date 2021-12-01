package com.origogi.booksearch.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.origogi.booksearch.R
import com.origogi.booksearch.databinding.RvItemBookBinding
import com.origogi.booksearch.model.Book

class RvAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var books : List<Book> = emptyList()

    fun update(newBooks : List<Book>) {

        val diffCallback = Diff(books, newBooks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        books = newBooks

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<RvItemBookBinding>(
            LayoutInflater.from(parent.context),
            R.layout.rv_item_book, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyViewHolder).bind(books[position])
    }

    override fun getItemCount() = books.size


    private class MyViewHolder(private val binding : RvItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book : Book) {
            binding.item = book
            binding.executePendingBindings()
        }
    }

    private class Diff(
        private val oldItems: List<Book>,
        private val newItems: List<Book>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int =
            oldItems.size

        override fun getNewListSize(): Int =
            newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].isbn13 == newItems[newItemPosition].isbn13
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }
}