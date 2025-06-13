package mp.iamuproject.api

import retrofit2.Call
import retrofit2.http.GET

const val API_URL = "https://feeds.feedburner.com/"

interface HNApi {
    @GET("TheHackersNews")
    fun fetchFeed(): Call<HNFeed>
}