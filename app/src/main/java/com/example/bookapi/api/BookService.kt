package com.example.bookapi.api

import com.example.bookapi.Model.BestSellerDto
import com.example.bookapi.Model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {


    @GET("/api/search.api?output=json") //해당 URL 주소가 가진정보를 검색하기 위해 서버 측에 요청함

    fun getBooksByName(
        @Query("key")//인증키 넘기기 (접근이가능한지 유효했을때)
        apikey: String,
        @Query("query")
        keyword : String




        ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?output=json&categoryId=100")

    fun getBestSellerBooks(
        @Query("key") apiKey : String

    ): Call<BestSellerDto>

}