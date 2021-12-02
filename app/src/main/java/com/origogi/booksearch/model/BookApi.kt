package com.origogi.booksearch.model

import com.origogi.booksearch.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface BookApi {
    @GET("search/{keyword}/{page}")
    suspend fun getBooks(
        @Path("keyword") keyword: String,
        @Path("page") page: Int
    ): BookListResponse

    @GET("books/{isbn}")
    suspend fun getBook(
        @Path("isbn") isbn: String,
    ): BookResponse
}

data class BookListResponse (
    var error : Int =0,
    var total : Int = 0,
    var page: Int = 0,
    var books : List<Book> = emptyList()
)

data class BookResponse (
    var error : Int =0,
    val title: String,
    var subtitle: String = "",
    var authors: String = "",
    var publisher: String = "",
    var language: String = "",
    var isbn10: String = "",
    var isbn13: String = "",
    var pages: String = "",
    var year: String = "",
    var rating: String = "",
    var desc: String = "",
    var price: String = "",
    var image: String = "",
    var url: String = "",
)


object RetrofitService {

    val bookApi: BookApi by lazy {
        val client = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                val logger = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
                addInterceptor(logger)
            }
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.itbook.store/1.0/")
            .client(
                client
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(BookApi::class.java)
    }
}