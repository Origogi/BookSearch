package com.origogi.booksearch.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.origogi.booksearch.State
import com.origogi.booksearch.TAG
import com.origogi.booksearch.model.Book
import com.origogi.booksearch.model.RetrofitService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var includeWords: List<String> = emptyList()
    private var excludeWord = ""
    private val pageIndexerMap = mutableMapOf<String, PageIndexer>()

    private val _books: MutableLiveData<List<Book>> = MutableLiveData()
    val books: LiveData<List<Book>>
        get() = _books

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state

    private val _showProcessInd = MediatorLiveData<Boolean>().apply {
        false
        addSource(state) {
            value = state.value == State.LOADING && (books.value ?: emptyList()).isEmpty()
        }
        addSource(books) {
            value = state.value == State.LOADING && (books.value ?: emptyList()).isEmpty()
        }
    }
    val showProcessInd : LiveData<Boolean>
        get() = _showProcessInd

    fun search(words: List<String>, excWord: String) {
        Log.d(TAG, "search $words $excWord")
        viewModelScope.launch {
            pageIndexerMap.clear()
            includeWords = words

            includeWords.forEach {
                pageIndexerMap[it] = PageIndexer()
            }

            excludeWord = excWord
            _books.value = emptyList()
            fetch()
        }
    }

    fun loadMore() {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch {
            _state.value = State.LOADING
            includeWords
                .filter { word ->
                    pageIndexerMap[word]!!.hasNextPage()
                }
                .map { word ->
                    word to async {
                        RetrofitService.bookApi.getBooks(word, pageIndexerMap[word]!!.getNextPage())
                    }
                }.map {
                    val (word , job) = it
                    word to job.await()
                }.map {
                    val (word , response) = it
                    pageIndexerMap[word]!!.updateCount(
                        response.books.size,
                        response.total
                    )

                    response.books
                }
                .map { books ->
                    if (excludeWord.isNotBlank()) {
                        books.filter {
                            !it.title.lowercase().contains(excludeWord.lowercase())
                        }
                    } else {
                        books
                    }
                }.forEach { books ->
                    if (books.isNotEmpty()) {
                        _books.value = (_books.value ?: emptyList()) + books
                    }
                }
            _state.value = State.IDLE
        }
    }

    private class PageIndexer {
        private var currentCount = 0
        private var maxCount = 0
        private var currentPage = 1

        fun hasNextPage(): Boolean {
            Log.d(TAG, "$currentCount/$maxCount")
            return (currentCount < maxCount) || (currentCount == 0)
        }

        fun getNextPage(): Int {
            return currentPage++
        }

        fun updateCount(responseCurrentCount: Int, responseMaxCount: Int) {
            currentCount += responseCurrentCount
            maxCount = responseMaxCount
        }
    }
}