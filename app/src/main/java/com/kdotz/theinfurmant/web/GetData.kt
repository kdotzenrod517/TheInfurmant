package com.kdotz.theinfurmant.web

import com.kdotz.theinfurmant.model.DogImageResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GetData {

    @Headers("X-Api-Key: c2ba9c34-0e8a-4c62-bceb-8793013a6fbe")
    @GET("images/search?limit=100&order=ASC")
    fun getImage(@Query("page") limit : String) : Observable<List<DogImageResponse>>
}