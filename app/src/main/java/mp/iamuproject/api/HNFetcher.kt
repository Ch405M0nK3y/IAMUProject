@file:Suppress("DEPRECATION")

package mp.iamuproject.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import mp.iamuproject.HN_PROVIDER_CONTENT_URI
import mp.iamuproject.HNReceiver
import mp.iamuproject.framework.sendBroadcast
import mp.iamuproject.handler.downloadImage
import mp.iamuproject.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class HNFetcher(private val context: Context) {
    private var hnApi: HNApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
        hnApi = retrofit.create(HNApi::class.java)
    }

    fun fetchItems(count: Int) {
        val request = hnApi.fetchFeed()

        request.enqueue(object: Callback<HNFeed> {
            override fun onResponse(
                call: Call<HNFeed>,
                response: Response<HNFeed>,
            ) {
                response.body()?.channel?.items?.take(count)?.let { populateItems(it) }
            }
            override fun onFailure(call: Call<HNFeed>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
            }
        })
    }

    private fun populateItems(hnItems: List<HNItem>) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            hnItems.forEach { item ->
                val imageUrl = item.enclosure?.takeIf { it.type?.startsWith("image/") ?: false  }?.url
                val picturePath = imageUrl?.let { downloadImage(context, it) } ?: ""
                val values = ContentValues().apply {
                    put(Item::title.name, item.title)
                    put(Item::description.name, item.description)
                    put(Item::picturePath.name, picturePath )
                    put(Item::date.name, item.pubDate)
                    put(Item::author.name, item.author)
                    put(Item::read.name, false)
                }
                context.contentResolver.insert(
                    HN_PROVIDER_CONTENT_URI,
                    values
                )
            }
            context.sendBroadcast<HNReceiver>()
        }
    }
}