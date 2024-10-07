package he2b.be.startreview.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface StartHttpClient {

    @POST("gql/alpha")
    suspend fun getData(
        @Body info: StartConnexion,
        @Header("Content-Type") content: String = "application/json",
        @Header("Authorization") token: String = "Bearer cfbd011df2bb754f9cc64ff047504365"
    ) : StartResponse
}