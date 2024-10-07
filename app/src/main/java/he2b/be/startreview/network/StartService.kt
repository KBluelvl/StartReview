package he2b.be.startreview.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object StartService {
    private const val baseURL = "https://api.start.gg/"
    val startClient : StartHttpClient
    init {
        // create a converter JSON -> Kotlin
        val jsonConverter = MoshiConverterFactory.create()
        // create a Retrofit builder
        val retrofitBuilder : Retrofit.Builder =
            Retrofit.Builder().addConverterFactory(jsonConverter)
                .baseUrl(baseURL)
        // create a Retrofit instance
        val retrofit : Retrofit = retrofitBuilder.build()
        // create our client
        startClient = retrofit.create(StartHttpClient::class.java)
    }
}