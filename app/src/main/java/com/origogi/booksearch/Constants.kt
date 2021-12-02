package com.origogi.booksearch

import com.origogi.booksearch.model.BookDetail

const val TAG = "KJT"

enum class State {
    IDLE, LOADING
}

val dummyData = BookDetail(
    "Learning Android Application Programming for the Kindle Fire",
    "A Hands-On Guide to Building Your First Android Application",
    "Lauren Darcey, Shane Conder",
    "Addison-Wesley",
    "English",
    "032183397X",
    "9780321833976",
    "352",
    "2012",
    "4",
    "In this book, bestselling Android programming authors Lauren Darcey and Shane Conder teach you every skill and technique you need to write production-quality apps for Amazon Kindle Fire, the world's hottest Android tablet. You'll learn the very best way: by building a complete app from start to fini...",
    "$11.96",
    "https://itbook.store/img/books/9780321833976.png",
    "https://itbook.store/books/9780321833976"
)