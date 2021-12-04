package com.origogi.booksearch.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.origogi.booksearch.State
import com.origogi.booksearch.TAG
import com.origogi.booksearch.model.Book
import com.origogi.booksearch.model.RetrofitService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var includeWords: List<String> = emptyList()
    private var excludeWord = ""
    private val pageIndexerMap = mutableMapOf<String, PageIndexer>()

    private val _books: MutableLiveData<LinkedHashSet<Book>> = MutableLiveData()
    val books: LiveData<List<Book>> = MediatorLiveData<List<Book>>().apply {
        value = emptyList()
        addSource(_books) {
            value = _books.value?.toList()
        }
    }


    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _showProcessInd = MediatorLiveData<Boolean>().apply {
        value = false
        addSource(_state) {
            value = _state.value == State.LOADING && (_books.value ?: emptyList()).isEmpty()
        }
        addSource(_books) {
            value = _state.value == State.LOADING && (_books.value ?: emptyList()).isEmpty()
        }
    }
    val showProcessInd : LiveData<Boolean>
        get() = _showProcessInd

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e(TAG,"$exception")
        _state.value = State.IDLE

        viewModelScope.launch {
            _errorMessage.value = exception.message
            delay(1000)
            _errorMessage.value = ""
        }
    }


    fun search(words: List<String>, excWord: String) {
        Log.d(TAG, "search $words $excWord")
        viewModelScope.launch {
            pageIndexerMap.clear()
            includeWords = words

            includeWords.forEach {
                pageIndexerMap[it] = PageIndexer()
            }

            excludeWord = excWord
            _books.value = linkedSetOf()
            fetch()
        }
    }

    fun loadMore() {
        fetch()
    }

    private fun fetch() {
        viewModelScope.launch(handler) {
            _state.value = State.LOADING

            val beforeSize = _books.value?.size ?:0
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
                        _books.value = (_books.value ?: linkedSetOf()).apply {
                            addAll(books)
                        }
                    }
                }
            _state.value = State.IDLE

            val afterSize = _books.value?.size ?:0

            Log.d(TAG, "size changed $beforeSize => $afterSize")
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