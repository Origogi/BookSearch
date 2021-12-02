package com.origogi.booksearch.model

import com.origogi.booksearch.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApi {
    @GET("{keyword}/{page}")
    suspend fun getBooks(
        @Path("keyword") keyword: String,
        @Path("page") page: Int
    ): Response
}

data class Response (
    var error : Int =0,
    var total : Int = 0,
    var page: Int = 0,
    var books : List<Book> = emptyList()
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
            .baseUrl("https://api.itbook.store/1.0/search/")
            .client(
                client
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(BookApi::class.java)
    }
}