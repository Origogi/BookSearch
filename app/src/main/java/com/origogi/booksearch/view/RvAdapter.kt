package com.origogi.booksearch.view

import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.origogi.booksearch.R
import com.origogi.booksearch.State
import com.origogi.booksearch.TAG
import com.origogi.booksearch.databinding.RvItemBookBinding
import com.origogi.booksearch.model.Book
import kotlinx.coroutines.*

class RvAdapter(private val loadDataMore: () -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var books: List<Book> = emptyList()
    private var currentState = State.IDLE
    private val throttle = Throttle(1000L)

    fun updateBooks(newBooks: List<Book>) {

        val diffCallback = Diff(books, newBooks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        books = newBooks

        diffResult.dispatchUpdatesTo(this)
    }

    fun updateState(state: State) {
        Log.d(TAG, "change state $currentState => $state")
        currentState = state
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

        if (position > itemCount - 10 && currentState == State.IDLE) {
            throttle.launch {
                loadDataMore()
            }
        }
    }

    override fun getItemCount() = books.size


    private class MyViewHolder(private val binding: RvItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.item = book
            binding.root.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java).apply {
                    putExtra("isbn", book.isbn13)
                }
                it.context.startActivity(intent)
            }
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


    private class Throttle(
        private val delayMs: Long,
        private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    ) {
        private var job: Job? = null
        private var nextMs: Long = 0
        fun launch(func: () -> Unit) {
            val current = SystemClock.uptimeMillis()
            if (nextMs < current) {
                nextMs = current + delayMs
                func()
                job?.cancel()
            } else {
                job?.cancel()
                job = coroutineScope.launch {
                    delay(nextMs - current)
                    nextMs = SystemClock.uptimeMillis() + delayMs
                    func()
                }
            }

        }
    }

}