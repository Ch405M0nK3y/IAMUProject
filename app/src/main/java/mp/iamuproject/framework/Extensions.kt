package mp.iamuproject.framework

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import mp.iamuproject.HN_PROVIDER_CONTENT_URI
import mp.iamuproject.model.Item

fun View.applyAnimation(id: Int) {
    startAnimation(AnimationUtils.loadAnimation(context, id))
}

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}
inline fun <reified T : Activity> Context.startActivity(
    key: String,
    value: Int
) {
    startActivity(
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(key, value)
        }
    )
}

inline fun<reified T: BroadcastReceiver> Context.sendBroadcast() {
    sendBroadcast(Intent(this, T::class.java))
}

fun Context.setBooleanPreference(key: String, value: Boolean = true) {
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()
}

fun Context.getBooleanPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)

fun Context.isOnline() : Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { cap ->
            return cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }
    return false
}

fun callDelayed(delay: Long, work: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        work,
        delay
    )
}

@SuppressLint("Range")
fun Context.fetchItems(): MutableList<Item>{
    val items = mutableListOf<Item>()
    val cursor = contentResolver?.query(
        HN_PROVIDER_CONTENT_URI,
        null,
        null,
        null,
        null
    )
    cursor?.use {
        while (it.moveToNext()) {
            val item = Item(
                _id = it.getLong(it.getColumnIndex(Item::_id.name)),
                title = it.getString(it.getColumnIndex(Item::title.name)) ?: "",
                description = it.getString(it.getColumnIndex(Item::description.name)) ?: "",
                picturePath = it.getString(it.getColumnIndex(Item::picturePath.name)) ?: "",
                date = it.getString(it.getColumnIndex(Item::date.name)) ?: "",
                author = it.getString(it.getColumnIndex(Item::author.name)) ?: "",
                read = it.getInt(it.getColumnIndex(Item::read.name)) == 1,
                guid = it.getString(it.getColumnIndex(Item::guid.name)) ?: "",
                url = it.getString(it.getColumnIndex(Item::url.name)) ?: ""
            )
            items.add(item)
        }
    }
    return items
}


