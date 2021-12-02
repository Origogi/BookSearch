package com.origogi.booksearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

            includeWords
                .filter { word ->
                    pageIndexerMap[word]!!.hasNextPage()
                }
                .map { word ->
                    async {
                        RetrofitService.bookApi.getBooks(word, pageIndexerMap[word]!!.getNextPage())
                    }
                }.map {
                    it.await()
                }.mapIndexed { i, response ->

                    pageIndexerMap[includeWords[i]]!!.updateCount(
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
                    _books.value = (_books.value ?: emptyList()) + books
                }
        }
    }

    private class PageIndexer {
        private var currentCount = 0
        private var maxCount = 0
        private var currentPage = 1

        fun hasNextPage(): Boolean {
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